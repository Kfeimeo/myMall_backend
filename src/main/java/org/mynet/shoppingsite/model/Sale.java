package org.mynet.shoppingsite.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Sale {
    private Long productId;
    private Long quantity;
}
