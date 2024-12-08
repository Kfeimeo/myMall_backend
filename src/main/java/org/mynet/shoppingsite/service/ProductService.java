package org.mynet.shoppingsite.service;

import org.mynet.shoppingsite.model.Category;
import org.mynet.shoppingsite.model.Product;
import org.mynet.shoppingsite.model.ProductNotFoundException;
import org.mynet.shoppingsite.repository.CategoryRepository;
import org.mynet.shoppingsite.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }


    // 获取所有商品
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 通过ID获取单个商品
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    public BigDecimal getProductPriceById(Long id) {
        return productRepository.findById(id).map(Product::getPrice).orElse(null);
    }
    // 添加一个新商品
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // 更新商品
    public Product updateProduct(Long id, Product updatedProduct) {
        // 这里可以先检查商品是否存在，若存在则更新
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProduct.getName());
                    product.setPrice(updatedProduct.getPrice());
                    product.setDescription(updatedProduct.getDescription());
                    product.setImage_url(updatedProduct.getImage_url());
                    product.setCategory_id(updatedProduct.getCategory_id());
                    product.setStock(updatedProduct.getStock());
                    product.setIs_active(updatedProduct.isIs_active());
                    return productRepository.save(product);
                }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product toggleProductStatus(Long productId, Boolean isActive) {
        // 使用 Optional 防止空指针异常
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("商品未找到，ID：" + productId));

        // 如果状态相同，则直接返回，不做更新
        if (product.isIs_active() == isActive) {
            return product;
        }

        // 更新状态并保存
        product.setIs_active(isActive);
        return productRepository.save(product);
    }


    // 删除商品
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
