package org.mynet.shoppingsite.controller;

import org.mynet.shoppingsite.model.Customer;
import org.mynet.shoppingsite.service.AdminService;
import org.mynet.shoppingsite.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    // 修改管理员密码
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updateCustomer(@PathVariable Long id, @RequestBody String password) {
        return ResponseEntity.ok(adminService.updatePassword(id, password));
    }

}
