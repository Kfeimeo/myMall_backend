package org.mynet.shoppingsite.controller;

import org.mynet.shoppingsite.model.Order;
import org.mynet.shoppingsite.model.OrderItem;
import org.mynet.shoppingsite.model.OrderRequest;
import org.mynet.shoppingsite.model.Sale;
import org.mynet.shoppingsite.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 创建订单
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) {
        Order order = orderService.addOrder(orderRequest);
        return ResponseEntity.ok(order);
    }
    // 获取所有订单
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        try {
            List<Order> orders = orderService.getAllOrders(); // 从服务层获取所有订单
            if (orders.isEmpty()) {
                return ResponseEntity.noContent().build(); // 返回204 No Content，表示没有数据
            }
            return ResponseEntity.ok(orders); // 返回200 OK 和订单列表
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // 返回500服务器错误
        }
    }

    // 获取某用户的所有订单
    @GetMapping("/customer/{userId}/orders")
    public ResponseEntity<List<Order>> getAllOrders(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByCustomerId(userId);
        return ResponseEntity.ok(orders);
    }

    // 获取单个订单
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        Optional<Order> order = orderService.getOrderById(orderId);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //获取指定订单的所有订单项
    @GetMapping("/items")
    public ResponseEntity<List<OrderItem>> getOrderItems(@RequestParam String orderIds) {
        String[] idArray = orderIds.split(",");
        List<Long> ids = new ArrayList<>();
        for (String id : idArray) {
            try {
                ids.add(Long.parseLong(id.trim()));
            } catch (NumberFormatException e) {

                // 处理异常，如忽略无效ID或返回错误
                // 这里简单地忽略无效ID
            }
        }
        List<OrderItem> orderItems = orderService.getOrderItemsByOrderIds(ids);
        return ResponseEntity.ok(orderItems);
    }
    // 更新订单状态
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @RequestParam Boolean status) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, status);
        return updatedOrder != null ? ResponseEntity.ok(updatedOrder) : ResponseEntity.notFound().build();
    }

    // 取消订单
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        boolean success = orderService.deleteOrder(orderId);
        if (success) {
            return ResponseEntity.ok("订单已取消");
        } else {
            return ResponseEntity.status(400).body("取消订单失败，订单已发货或不存在");
        }
    }
    @GetMapping("/sales")
    public ResponseEntity<Sale> getTotalSales() {
        Sale totalSales =orderService.calculateTotalSales();
        return ResponseEntity.ok(totalSales);
    }
}
