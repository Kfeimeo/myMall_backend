package org.mynet.shoppingsite.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;


// 销售数据类
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccumulatedSale {

    private Map<Long,Long> sale;//key:id value:quantity
    private BigDecimal totalSales;
    private Map<Long,Product> productDetail;//key:id value:product

}

