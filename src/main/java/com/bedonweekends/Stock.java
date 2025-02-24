package com.bedonweekends;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    private String name;
    private Map<String, Long> stockPricePerTimeMap;
}
