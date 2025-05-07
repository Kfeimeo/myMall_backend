package org.mynet.shoppingsite.controller;

import org.mynet.shoppingsite.model.Product;
import org.mynet.shoppingsite.service.ProductService;
import org.mynet.shoppingsite.service.UserSimilarityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    private final UserSimilarityService userSimilarityService;

    @Autowired
    public ProductController(ProductService productService, UserSimilarityService userSimilarityService) {
        this.productService = productService;
        this.userSimilarityService = userSimilarityService;
    }

    // 获取所有商品
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products); // 返回 200 状态码，数据为产品列表
    }
    //获取某个商家的所有销售的商品
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Product>> getProductsBySellerId(@PathVariable Long sellerId) {
        List<Product> products = productService.getProductsBySellerId(sellerId);
        return ResponseEntity.ok(products);
    }

    // 获取单个商品
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    //添加产品
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

    //推荐产品
    @GetMapping("/recommend/{customerId}")
    public ResponseEntity<List<Product>> recommendProducts(@PathVariable Long customerId) {
        List<Product> recommendedProductIds = userSimilarityService.recommendProductsForUser(customerId);
        return ResponseEntity.ok(recommendedProductIds);
    }


}
