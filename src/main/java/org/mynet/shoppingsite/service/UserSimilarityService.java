package org.mynet.shoppingsite.service;
import org.mynet.shoppingsite.model.*;
import org.mynet.shoppingsite.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserSimilarityService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    /**
     * 计算两个用户评分列表之间的皮尔逊相关系数
     */
    public Double calculatePearsonCorrelation(Map<Long, Integer> user1Ratings, Map<Long, Integer> user2Ratings) {
        // 找到两个用户都评价过的商品
        Set<Long> commonProducts = new HashSet<>(user1Ratings.keySet());
        commonProducts.retainAll(user2Ratings.keySet());

        int n = commonProducts.size();
        if (n == 0) return 0.0; // 没有共同评价的商品，相关性为0

        double sumUser1 = 0, sumUser2 = 0, sumUser1Sq = 0, sumUser2Sq = 0, sumProduct = 0;

        for (Long productId : commonProducts) {
            int rating1 = user1Ratings.get(productId);
            int rating2 = user2Ratings.get(productId);

            sumUser1 += rating1;
            sumUser2 += rating2;
            sumUser1Sq += Math.pow(rating1, 2);
            sumUser2Sq += Math.pow(rating2, 2);
            sumProduct += rating1 * rating2;
        }

        double numerator = sumProduct - (sumUser1 * sumUser2 / n);
        double denominator = Math.sqrt((sumUser1Sq - Math.pow(sumUser1, 2) / n) * (sumUser2Sq - Math.pow(sumUser2, 2) / n));

        return (denominator == 0) ? 0 : numerator / denominator;
    }

    /**
     * 计算并返回与目标用户最相似的用户的ID
     */
    public Long findMostSimilarUser(Long userId) {
        Map<Long, Integer> targetUserRatings = getUserRatingsMap(userId);
        List<Customer> users = customerRepository.findAll();

        double maxSimilarity = -1.0; // 初始化为负值，因为皮尔逊相关系数范围是[-1,1]
        Long mostSimilarUserId = null;

        for (Customer user : users) {
            if (!user.getId().equals(userId)) {
                Map<Long, Integer> currentUserRatings = getUserRatingsMap(user.getId());
                Double similarity = calculatePearsonCorrelation(targetUserRatings, currentUserRatings);

                // 更新最大相似度和对应用户ID
                if (similarity > maxSimilarity) {
                    maxSimilarity = similarity;
                    mostSimilarUserId = user.getId();
                }
            }
        }

        return mostSimilarUserId;
    }

    /**
     * 为指定用户推荐商品，确保推荐结果无重复
     */
    public List<Product> recommendProductsForUser(Long customerId) {
        // 使用Set来保证商品的唯一性
        Set<Product> recommendedProductsSet = new HashSet<>();
        try {
            // 获取用户已购买的商品ID列表
            Set<Long> userPurchasedProductIds = getUserPurchasedProductIds(customerId);

            // 找到最相似的用户
            Long similarUserId = findMostSimilarUser(customerId);
            if (similarUserId != null) {
                // 获取相似用户的订单
                List<Order> orders = orderRepository.findByCustomerId(similarUserId);

                // 收集推荐商品，排除用户已购买的商品
                Set<Long> processedProductIds = new HashSet<>(); // 跟踪已处理的商品ID

                for (Order order : orders) {
                    List<OrderItem> orderItems = orderItemRepository.findByOrderId(List.of(order.getOrderId()));
                    for (OrderItem orderItem : orderItems) {
                        Long productId = orderItem.getProductId();
                        // 检查商品是否已处理以及用户是否已购买
                        if (!processedProductIds.contains(productId) && !userPurchasedProductIds.contains(productId)) {
                            processedProductIds.add(productId);
                            productRepository.findById(productId).ifPresent(recommendedProductsSet::add);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("推荐商品时出错: " + e.getMessage());
            e.printStackTrace();
        }

        // 转换为List并返回
        return new ArrayList<>(recommendedProductsSet);
    }

    /**
     * 获取用户购买过的商品ID集合
     */
    private Set<Long> getUserPurchasedProductIds(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        return orders.stream()
                .flatMap(order -> orderItemRepository.findByOrderId(List.of(order.getOrderId())).stream())
                .map(OrderItem::getProductId)
                .collect(Collectors.toSet());
    }

    /**
     * 获取用户的商品评分映射 (商品ID -> 购买数量)
     */
    private Map<Long, Integer> getUserRatingsMap(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        return orders.stream()
                .flatMap(order -> orderItemRepository.findByOrderId(List.of(order.getOrderId())).stream())
                .collect(Collectors.groupingBy(OrderItem::getProductId, Collectors.summingInt(OrderItem::getQuantity)));
    }
}
