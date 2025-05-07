package org.mynet.shoppingsite.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Java 端
@Data
@AllArgsConstructor
public class SalesTrendDTO {
    private Map<Long, ArrayList<Long>> saleTrend;
    private ArrayList<LocalDateTime> timePoints;
}
