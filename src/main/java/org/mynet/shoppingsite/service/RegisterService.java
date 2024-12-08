package org.mynet.shoppingsite.service;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.mynet.shoppingsite.model.Admin;
import org.mynet.shoppingsite.model.Customer;
import org.mynet.shoppingsite.model.Seller;
import org.mynet.shoppingsite.repository.AdminRepository;
import org.mynet.shoppingsite.repository.CustomerRepository;
import org.mynet.shoppingsite.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    private AdminRepository adminRepository;
    private SellerRepository sellerRepository;
    private CustomerRepository customerRepository;
    @Autowired
    public RegisterService(AdminRepository adminRepository, SellerRepository sellerRepository, CustomerRepository customerRepository) {
        this.adminRepository = adminRepository;
        this.sellerRepository = sellerRepository;
        this.customerRepository = customerRepository;
    }

    public void registerUser(String username, String password, String role) {
        switch (role){
            case "admin":
                adminRepository.save(new Admin(username, password));

            case "seller":
                sellerRepository.save(new Seller(username, password));

            case "customer":
                customerRepository.save(new Customer(username, password));
        }
    }
}
