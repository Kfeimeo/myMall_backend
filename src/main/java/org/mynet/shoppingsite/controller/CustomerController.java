package org.mynet.shoppingsite.controller;

import org.mynet.shoppingsite.model.Customer;
import org.mynet.shoppingsite.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.mynet.shoppingsite.model.CustomerUpdate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;
    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // 获取用户资料
    @GetMapping("/profile")
        public ResponseEntity<Map<String, Object>> getCustomerProfile(@RequestParam String username) {
        try {
            Customer customer = customerService.getCustomerByUsername(username);
            if (customer == null || customer.getId() == 0) {
                return ResponseEntity.status(404).body(Map.of("message", "用户未找到"));
            }
            return ResponseEntity.ok(Map.of(
                    "id", customer.getId(),
                    "username", customer.getUsername(),
                    "email", customer.getEmail()
                    , "address", customer.getAddress()
                    , "password", customer.getPassword()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "服务器错误"));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<Map<String, String>> updateCustomerProfile(@RequestBody CustomerUpdate udpate) {
        try {
            if(udpate.id == null){
                return ResponseEntity.status(400).body(Map.of("message", "用户ID不能为空"));
            }
            Customer updatedCustomer = new Customer(udpate);
            customerService.updateCustomer(updatedCustomer);
            // 返回更新成功的消息
            return ResponseEntity.ok(Map.of("message", "用户信息更新成功"));
        } catch (Exception e) {
            // 打印详细的异常信息
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "服务器错误"));
        }
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long userId) {
        // 查找用户并删除
        Optional<Customer> customer = Optional.ofNullable(customerService.getCustomerById(userId));
        if (customer.isPresent()) {
            customerService.deleteCustomerById(userId);
            return ResponseEntity.status(HttpStatus.OK).body("账号删除成功");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("用户不存在");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }


}
