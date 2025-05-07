package org.mynet.shoppingsite.service;

import org.mynet.shoppingsite.model.*;
import org.mynet.shoppingsite.repository.OrderRepository;
import org.mynet.shoppingsite.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderService(ProductService productService, OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    // 创建新订单
    public Order addOrder(OrderRequest orderRequest) {
        // 1. 创建订单基础信息
        Order order = new Order();
        order.setCustomerId(orderRequest.customerID);
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderStatus(false); // 初始状态

        // 2. 计算总价（需先获取Product对象）
        List<Product> products = orderRequest.items.entrySet().stream()
                .map(entry -> productService.getProductById(entry.getValue()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        BigDecimal totalPrice = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalPrice(totalPrice);

        // 3. 保存订单（先保存Order以生成ID）
        Order savedOrder = orderRepository.save(order);

        // 4. 构建OrderItem列表（建立对象关联）
        List<OrderItem> orderItems = orderRequest.items.entrySet().stream()
                .map(entry -> {
                    Product product = productService.getProductById(entry.getValue())
                            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + entry.getValue()));

                    return new OrderItem(
                            savedOrder, // 关联Order对象
                            product,   // 关联Product对象
                            Math.toIntExact(entry.getKey()) // quantity
                    );
                })
                .toList();

        // 5. 保存订单项
        orderItemRepository.saveAll(orderItems);

        return savedOrder;
    }

    // 获取订单列表
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // 获取单个订单
    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    // 获取某一个用户的的订单
    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
    // 根据订单ID列表获取订单项
    public List<OrderItem> getOrderItemsByOrderIds(List<Long> orderIds) {
        return orderItemRepository.findByOrderId(orderIds);
    }
    // 更新订单状态
    public Order updateOrderStatus(Long orderId, Boolean status) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            order.get().setOrderStatus(status);
            return orderRepository.save(order.get());
        }
        return null;
    }

    // 取消订单
    public boolean deleteOrder(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if (!order.getOrderStatus()) { // 如果订单未发货，才允许取消
                orderRepository.delete(order);
                return true;
            }
        }
        return false;  // 订单已发货或不存在
    }
    public List<Order> getOrders(Long customerId, String startDate, String endDate) {
        if (customerId != null) {
            return orderRepository.findByCustomerId(customerId);
        }
        if (startDate != null && endDate != null) {
            return orderRepository.findByDateRange(startDate, endDate);
        }
        return orderRepository.findAll();
    }


    public List<Order> getOrdersBySellerId(Long userId) {
        return orderRepository.findBySellerId(userId);
    }
}
