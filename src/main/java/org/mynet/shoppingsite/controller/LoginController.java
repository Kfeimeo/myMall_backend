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

    @PostMapping("/")
    public Map<String, Object> Login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        String role = loginData.get("role");
        Map<String, Object> response = new HashMap<>();
        boolean isValidUser = loginService.validateLogin(username, password,role);

        if (isValidUser) {
            response.put("success", true);
            response.put("message", "登录成功");
            response.put("userId", loginService.findIdByUsername(username,role));
        } else {
            response.put("success", false);
            response.put("message", "用户名或密码错误");

        }

        return response;
    }


}
