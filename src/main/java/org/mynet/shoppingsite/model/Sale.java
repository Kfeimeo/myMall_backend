package org.mynet.shoppingsite.model;

import java.math.BigDecimal;

public class Sale {
    private BigDecimal totalSalesAmount;
    private Long totalSalesQuantity;

    // 构造函数、getter和setter
    public Sale(BigDecimal totalSalesAmount, Long totalSalesQuantity) {
        this.totalSalesAmount = totalSalesAmount;
        this.totalSalesQuantity = totalSalesQuantity;
    }

    public BigDecimal getTotalSalesAmount() {
        return totalSalesAmount;
    }

    public void setTotalSalesAmount(BigDecimal totalSalesAmount) {
        this.totalSalesAmount = totalSalesAmount;
    }

    public Long getTotalSalesQuantity() {
        return totalSalesQuantity;
    }

    public void setTotalSalesQuantity(Long totalSalesQuantity) {
        this.totalSalesQuantity = totalSalesQuantity;
    }
}
