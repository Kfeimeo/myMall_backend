package org.mynet.shoppingsite.service;

import lombok.AllArgsConstructor;
import org.mynet.shoppingsite.model.Admin;
import org.mynet.shoppingsite.repository.AdminRepository;
import org.springframework.stereotype.Service;
@AllArgsConstructor
@Service
public class AdminService {
    AdminRepository  adminRepository;

    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    //修改密码
    public Boolean updatePassword(Long adminId, String newPassword) {
        Admin admin = adminRepository.findById(adminId).orElse(null);
        if (admin != null) {
            admin.setPassword(newPassword);
            adminRepository.save(admin);
            return true;
        } else {
            return false;
        }
    }
}
