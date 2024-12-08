package org.mynet.shoppingsite.controller;

import org.mynet.shoppingsite.model.User;
import org.mynet.shoppingsite.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/admin")
    public Map<String, Object> AdminLogin(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        Map<String, Object> response = new HashMap<>();
        boolean isValidUser = loginService.validateLogin(username, password,"ADMIN");

        if (isValidUser) {
            response.put("success", true);
            response.put("message", "登录成功");
        } else {
            response.put("success", false);
            response.put("message", "用户名或密码错误");

        }

        return response;
    }

   @PostMapping("/seller")
   public Map<String, Object> SellerLogin(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        Map<String, Object> response = new HashMap<>();
        boolean isValidUser = loginService.validateLogin(username, password,"SELLER");

        if (isValidUser) {
            response.put("success", true);
            response.put("message", "登录成功");
        } else {
            response.put("success", false);
            response.put("message", "用户名或密码错误");
        }

        return response;
   }

   @PostMapping("/customer")
    public Map<String, Object> CustomerLogin(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        Map<String, Object> response = new HashMap<>();
        boolean isValidUser = loginService.validateLogin(username, password,"CUSTOMER");
        if (isValidUser) {
            response.put("success", true);
            response.put("message", "登录成功");
            response.put("userID",loginService.findIdByUsername(username,"CUSTOMER"));
            response.put("email",loginService.getCustomerByUsername(username).getEmail());
            response.put("address",loginService.getCustomerByUsername(username).getAddress());
        } else {
            response.put("success", false);
            response.put("message", "用户名或密码错误");
        }
        return response;
   }


}
