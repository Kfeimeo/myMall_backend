package org.mynet.shoppingsite.service;

import org.mynet.shoppingsite.model.Customer;
import org.mynet.shoppingsite.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.mynet.shoppingsite.repository.AdminRepository;
import org.mynet.shoppingsite.repository.SellerRepository;
import org.mynet.shoppingsite.repository.CustomerRepository;

@Service
public class LoginService {

    private final AdminRepository adminRepository;

    private final SellerRepository sellerRepository;

    private final CustomerRepository customerRepository;

    @Autowired
    public LoginService(AdminRepository adminRepository, SellerRepository sellerRepository, CustomerRepository customerRepository) {
        this.adminRepository = adminRepository;
        this.sellerRepository = sellerRepository;
        this.customerRepository = customerRepository;
    }
    public Long findIdByUsername(String username, String role) {
        return switch (role) {
            case ("ADMIN") -> adminRepository.findByUsername(username).getId();
            case ("SELLER")-> sellerRepository.findByUsername(username).getId();
            case ("CUSTOMER") -> customerRepository.findByUsername(username).getId();
            default -> null;
        };
    }
    public Boolean validateLogin(String username,String password, String role) {
        User user_;
        return switch (role) {
            case ("ADMIN") -> {
                user_ = adminRepository.findByUsername(username);
                yield user_ != null && user_.getPassword().equals(password);
            }
            case ("SELLER") -> {
                user_ = sellerRepository.findByUsername(username);

                yield user_ != null && user_.getPassword().equals(password);
            }
            case ("CUSTOMER") -> {
                user_ = customerRepository.findByUsername(username);
                yield user_ != null && user_.getPassword().equals(password);
            }
            default -> false;
        };

    }

    public Customer getCustomerByUsername(String username) {
        return customerRepository.findByUsername(username);
    }
};
