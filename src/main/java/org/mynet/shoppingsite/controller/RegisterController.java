package org.mynet.shoppingsite.controller;

import org.mynet.shoppingsite.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/register")
public class RegisterController
{
    RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService){
        this.registerService = registerService;
    }
    @PostMapping
    public Map<String, Object> registerUser(@RequestParam String username,
                                            @RequestParam String password,
                                            @RequestParam String role) {
        Map<String, Object> response = new HashMap<>();

        // 调用注册服务处理
        registerService.registerUser(username, password, role);

        response.put("success", true);
        return response;
    }

}
