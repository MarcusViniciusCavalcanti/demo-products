package com.exemple.products.demo.structure.dto.inputs;

import lombok.Data;

@Data
public class ParamsSearch {
    private String q;
    private Double min_price;
    private Double max_price;
}
