package com.bedonweekends;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockPricePerTime {
    private String timestamp;
    private double price;
    private boolean jump;
}
