package org.mynet.shoppingsite.service;

import com.github.signaflo.timeseries.TimeSeries;
import com.github.signaflo.timeseries.Ts;
import com.github.signaflo.timeseries.forecast.Forecast;
import com.github.signaflo.timeseries.model.arima.Arima;
import com.github.signaflo.timeseries.model.arima.ArimaCoefficients;
import lombok.RequiredArgsConstructor;
import org.mynet.shoppingsite.model.*;
import org.mynet.shoppingsite.repository.OrderItemRepository;
import org.mynet.shoppingsite.repository.ProductRepository;
import org.springframework.cglib.core.Local;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;


import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;


@Service
@RequiredArgsConstructor
public class SalesService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private AccumulatedSale getAccumulatedSale(List<Object[]> results) {
        Map<Long,Long> sales = new HashMap<>(Map.of());
        BigDecimal totalSales = BigDecimal.ZERO;
        Map<Long,Product> products = new HashMap<>(Map.of());
        // 打印查询结果，确保结果格式正确
        System.out.println("Query Results: ");
        for (Object[] result : results) {
            System.out.println("Product ID: " + result[0] + ", Quantity: " + result[1]);
        }
        for (Object[] result : results) {
            Product product = (Product) result[0];
            Long productId = product.getId();
            Long quantity = (Long) result[1];
            sales.put(productId, quantity);
            products.put(product.getId(),product);
            totalSales = totalSales.add(BigDecimal.valueOf(quantity).multiply(product.getPrice()));

        }

        return new AccumulatedSale(sales, totalSales,products);
    }
    // 1. 查询某商户所有商品的累计销量
    public AccumulatedSale getAccumulatedSales(Long sellerId) {
        List<Object[]> results = orderItemRepository.findAccumulatedSalesBySellerId(sellerId);

        return getAccumulatedSale(results);
    }



    // 2. 查询某商户在给定时间段内的所有商品累计销量
    public AccumulatedSale getAccumulatedSalesAndDateRange(Long sellerId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = orderItemRepository.findAccumulatedSalesBySellerIdAndDateRange(sellerId, startDate, endDate);

        return getAccumulatedSale(results);
    }

    //  3. 查询某商户在给定时间段内,按照给定时间单位分割得到的商品销量列表
    public SalesTrendDTO getAccumulatedSalesByTimeUnit(Long sellerId, LocalDateTime startDate, LocalDateTime endDate, String timeUnit) {
        Map<Long, ArrayList<Long>> salesTrend = new HashMap<>();
        ArrayList<LocalDateTime> timePoints = getTimePoints(startDate, endDate, timeUnit);

        // 获取所有有销售的商品
        ArrayList<Product> products = orderItemRepository.findSoldProductsBySellerIdAndDateRange(sellerId, startDate, endDate);

        // 初始化数据结构：每个产品对应一个与timePoints大小相同的列表
        products.forEach(product -> {
            salesTrend.put(product.getId(), new ArrayList<>(Collections.nCopies(timePoints.size(), 0L)));
        });

        // 按时间点查询并填充数据
        for (int i = 0; i < timePoints.size(); i++) {
            LocalDateTime timePoint = timePoints.get(i);
            LocalDateTime periodStart = timePoint;
            LocalDateTime periodEnd = getPeriodEnd(timePoint, timeUnit);

            List<Object[]> results = orderItemRepository.findAccumulatedSalesBySellerIdAndDateRange(
                    sellerId, periodStart, periodEnd);

            AccumulatedSale accumulatedSale = getAccumulatedSale(results);

            // 更新有销售的产品数据
            int finalI = i;
            accumulatedSale.getSale().forEach((productId, quantity) -> {
                if (salesTrend.containsKey(productId)) {
                    salesTrend.get(productId).set(finalI, quantity);
                }
            });
        }

        return new SalesTrendDTO(salesTrend, timePoints);
    }

    public ArrayList<Double> predictSales(ArrayList<Double> sales, LocalDateTime start) {
        // 1. 将 ArrayList<Double> 转换为 double[]
        double[] array = new double[sales.size()];
        for (int i = 0; i < sales.size(); i++) {
            array[i] = sales.get(i); // 自动拆箱 Double -> double
        }

        // 2. 构建时间序列并拟合 ARIMA 模型
        TimeSeries timeSeries = Ts.newMonthlySeries(start.getYear(), start.getMonthValue(), array);
        Arima.FittingStrategy fittingStrategy = Arima.FittingStrategy.CSSML;
        ArimaCoefficients coefficients = ArimaCoefficients.builder()
                .setMACoeffs(-0.6760904)
                .setSeasonalMACoeffs(-0.5718134)
                .setDifferences(1)
                .setSeasonalDifferences(1)
                .setSeasonalFrequency(12)
                .build();
        Arima model = Arima.model(timeSeries, coefficients, fittingStrategy);

        // 3. 预测未来 3 个月的数据
        Forecast forecast = model.forecast(6);
        double[] pointEstimates = forecast.pointEstimates().asArray();

        // 4. 将 double[] 转换为 ArrayList<Double>（推荐方式）
        ArrayList<Double> result = DoubleStream.of(pointEstimates)
                .boxed() // 将 double 转换为 Double
                .collect(Collectors.toCollection(ArrayList::new));
        // 5. 检查数据结果，如果有预测值是负数，要取绝对值
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i) < 0) {
                result.set(i, Math.abs(result.get(i)));
            }
        }

        return result;
    }

    private LocalDateTime getPeriodEnd(LocalDateTime timePoint, String timeUnit) {
        switch (timeUnit.toLowerCase()) {
            case "day":
                return timePoint.plusDays(1).minusNanos(1);
            case "month":
                return timePoint.plusMonths(1).minusNanos(1);
            case "year":
                return timePoint.plusYears(1).minusNanos(1);
            default:
                return timePoint.plusHours(1).minusNanos(1);
        }
    }
    private ArrayList<LocalDateTime> getTimePoints(LocalDateTime startDate, LocalDateTime endDate, String timeUnit) {
        ArrayList<LocalDateTime> timePoints = new ArrayList<>();
        switch (timeUnit) {
            case "day":
                while (startDate.isBefore(endDate)) {
                    timePoints.add(startDate);
                    startDate = startDate.plusDays(1);
                }
                timePoints.add(endDate);
                break;
            case "week":
                while (startDate.isBefore(endDate)) {
                    timePoints.add(startDate);
                    startDate = startDate.plusWeeks(1);
                }
                timePoints.add(endDate);
                break;
            case "month":
                while (startDate.isBefore(endDate)) {
                    timePoints.add(startDate);
                    startDate = startDate.plusMonths(1);
                }
                timePoints.add(endDate);
                break;
            case "year":
                while (startDate.isBefore(endDate)) {
                    timePoints.add(startDate);
                    startDate = startDate.plusYears(1);
                }
                timePoints.add(endDate);
                break;
            default:

                throw new IllegalArgumentException("Invalid time unit: " + timeUnit);
        }
        return timePoints;
    }


}
