package org.mynet.shoppingsite.service;

import org.mynet.shoppingsite.model.Order;
import org.mynet.shoppingsite.model.OrderItem;
import org.mynet.shoppingsite.model.OrderRequest;
import org.mynet.shoppingsite.model.Sale;
import org.mynet.shoppingsite.repository.OrderRepository;
import org.mynet.shoppingsite.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final ProductService productService;
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;

    @Autowired
    public OrderService(ProductService productService, OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    // 创建新订单
    public Order addOrder(OrderRequest orderrequest) {
        Long customerId = orderrequest.customerID;

        Order order = new Order();
        order.setCustomerId(customerId);
        order.setCreatedAt(java.time.LocalDateTime.now());
        BigDecimal totalPrice = orderrequest.items.values().stream()
                .map(productService::getProductPriceById)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalPrice(totalPrice);
        order.setOrderStatus(false);  // 假设订单初始状态是 "PENDING"
        //提取items中每一项为OrderItem
        List<OrderItem> orderItems = orderrequest.items.entrySet().stream()
                .map(entry -> new OrderItem(entry.getValue(), Math.toIntExact(entry.getKey())))
                .toList();
        Long Id=orderRepository.save(order).getOrderId();
        //将所有OrderItem的order_id设置为当前订单的id
        orderItems.forEach(item->item.setOrderId(Id));
        orderItemRepository.saveAll(orderItems);



        return order;
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
        return orderItemRepository.findByOrderIdIn(orderIds);
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
    public Sale calculateTotalSales() {
        // 计算所有订单的总销售额和总销售量
        BigDecimal totalSalesAmount = orderRepository.calculateTotalSalesAmount();
        Long totalSalesQuantity = orderRepository.calculateTotalSalesQuantity();

        return new Sale(totalSalesAmount, totalSalesQuantity);
    }
}
