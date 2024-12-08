package org.mynet.shoppingsite.service;

import org.mynet.shoppingsite.model.Customer;
import org.mynet.shoppingsite.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public Customer getCustomerByUsername(String username) {
        return customerRepository.findByUsername(username);
    }
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }
    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(id);
    }


    // 更新客户资料
    public void updateCustomer(Customer customer) {
        // 在数据库中查找用户
        Optional<Customer> existingCustomerOpt = customerRepository.findById(customer.getId());
        if (existingCustomerOpt.isEmpty()) {
            throw new RuntimeException("用户未找到");
        }

        // 获取到已有的用户对象
        Customer existingCustomer = existingCustomerOpt.get();

        // 更新信息
        existingCustomer.setUsername(customer.getUsername());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPassword(customer.getPassword());
        existingCustomer.setAddress(customer.getAddress());
        // 保存更新后的用户
        customerRepository.save(existingCustomer);
    }
    public Customer updateCustomer(Long Id, Customer customer) {
        // 在数据库中查找用户
        Optional<Customer> existingCustomerOpt = customerRepository.findById(Id);
        if (existingCustomerOpt.isEmpty()) {
            throw new RuntimeException("用户未找到");
        }

        // 获取到已有的用户对象
        Customer existingCustomer = existingCustomerOpt.get();

        // 更新信息
        existingCustomer.setUsername(customer.getUsername());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPassword(customer.getPassword());
        existingCustomer.setAddress(customer.getAddress());
        // 保存更新后的用户
        customerRepository.save(existingCustomer);
        return existingCustomer;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
