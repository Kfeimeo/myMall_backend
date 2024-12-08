package org.mynet.shoppingsite.controller;

import org.mynet.shoppingsite.model.Category;
import org.mynet.shoppingsite.model.Product;
import org.mynet.shoppingsite.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 获取所有商品
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products); // 返回 200 状态码，数据为产品列表
    }
    // 获取所有产品种类
    @GetMapping("/category")
    public ResponseEntity<List<Category>> getAllCategories() {
        try {
            List<Category> categories = productService.getAllCategories();
            if (categories == null) {
                categories = List.of(); // 防止空指针异常
            }
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            // 记录日志
            e.printStackTrace();
            // 返回错误信息
            return ResponseEntity.internalServerError().build();
        }
    }

    // 获取单个商品
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        try {
            Product savedProduct = productService.addProduct(product);
            return ResponseEntity.status(201).body(savedProduct);
        } catch (Exception e) {
            // 记录错误日志
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);  // 返回 500 错误
        }
    }


    // 更新商品信息
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    // 删除商品
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    // 上架/下架商品
    @PatchMapping("/{productId}/status")
    public ResponseEntity<Product> toggleProductStatus(@PathVariable Long productId, @RequestParam Boolean isActive) {
        Product updatedProduct = productService.toggleProductStatus(productId, isActive);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
