package org.mynet.shoppingsite.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mynet.shoppingsite.model.Seller;
import org.mynet.shoppingsite.repository.SellerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/seller")
public class SellerController {
    private final SellerRepository  sellerRepository;
    public SellerController(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @PostMapping("/update")
    public void updateSeller(Seller seller) {
        sellerRepository.save(seller);
    }
    @DeleteMapping("/delete")
    public void deleteSeller(Long id) {
        sellerRepository.deleteById(id);
    }
    @GetMapping("/all")
    public Iterable<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }
    @GetMapping("/get/{id}")
    public Seller getSellerById(Long id) {
        return sellerRepository.findById(id).orElse(null);
    }
    @GetMapping("/get/{username}")
    public Seller getSellerByUsername(@PathVariable String username) {
        return sellerRepository.findByUsername(username);
    }
@PostMapping("/create")
    public Seller createSeller(String username, String password) {
        Seller seller = new Seller(username, password);
        return sellerRepository.save(seller);
    }




}
