package org.mynet.shoppingsite.controller;

import org.mynet.shoppingsite.model.*;
import org.mynet.shoppingsite.service.LoginLogoutService;
import org.mynet.shoppingsite.service.OrderService;
import org.mynet.shoppingsite.service.SalesService;
import org.springframework.data.util.Pair;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class LogController {

    private final OrderService orderService;
    private final SalesService salesService;
    public LogController(OrderService orderService, SalesService salesService, LoginLogoutService loginLogoutService) {
        this.orderService = orderService;
        this.salesService = salesService;
        this.loginLogoutService = loginLogoutService;
    }

    @GetMapping("/api/logs")
    public List<Order> getLogs(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return orderService.getOrders(customerId, startDate, endDate);

    }
    @GetMapping("/api/sales/{sellerId}")
    ResponseEntity<AccumulatedSale> getSalesData( @PathVariable Long sellerId) {
        return ResponseEntity.ok(salesService.getAccumulatedSales(sellerId));
    }
    @GetMapping("/api/sales/{sellerId}/data-range")
    ResponseEntity<AccumulatedSale> getSalesDataWithDateRange(            @PathVariable Long sellerId,
                                                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        // 可选：添加日期验证逻辑
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("开始日期不能晚于结束日期");
        }

        return ResponseEntity.ok(salesService.getAccumulatedSalesAndDateRange(sellerId, start, end));
    }

    @GetMapping("/api/sales/{sellerId}/data-trend")
    public ResponseEntity<SalesTrendDTO> getSalesDataByTimeUnit(
            @PathVariable Long sellerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam String timeInterval) {


        if (start.isAfter(end)) {
            throw new IllegalArgumentException("开始日期不能晚于结束日期");
        }
        SalesTrendDTO dto =salesService.getAccumulatedSalesByTimeUnit(sellerId, start, end, timeInterval);
        return ResponseEntity.ok(dto);
    }


    // 响应异常处理（示例）
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    private final LoginLogoutService loginLogoutService;


    @GetMapping("/api/login-records")
    public ResponseEntity<List<LoginLogoutRecord>> getLoginRecords(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        List<LoginLogoutRecord> records;

        try {
            if (userId != null && startTime != null && endTime != null) {
                records = loginLogoutService.findByUserIdAndLoginTimeBetween(userId, startTime, endTime);
            } else if (userId != null) {
                records = loginLogoutService.findByUserId(userId);
            } else if (startTime != null && endTime != null) {
                records = loginLogoutService.findByLoginTimeBetween(startTime, endTime);
            } else {
                records = loginLogoutService.findAll();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }



        return ResponseEntity.ok(records);
    }

    // 创建新的登录记录
    @PostMapping("api/create-log")
    public ResponseEntity<LoginLogoutRecord> createRecord(@RequestBody LoginLogoutRecord record) {
        LoginLogoutRecord savedRecord = loginLogoutService.saveRecord(record);
        return ResponseEntity.ok(savedRecord);
    }

    // 获取会话统计信息
    @GetMapping("api/stats")
    public ResponseEntity<Map<String, Object>> getSessionStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        Map<String, Object> stats = loginLogoutService.getSessionStatistics(startTime, endTime);
        return ResponseEntity.ok(stats);
    }

    //预测销量
    @GetMapping("/api/predict-sales")
    public ResponseEntity<ArrayList<Double>> predictSales(
            @RequestParam ArrayList<Double> sales,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start
    )
    {
        ArrayList<Double> predictedSales = salesService.predictSales(sales, start);
        return ResponseEntity.ok(predictedSales);
    }

}
