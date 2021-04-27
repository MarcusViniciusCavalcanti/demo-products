package com.exemple.products.demo.structure.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductDto {

    private final UUID id;
    private final String name;
    private final String description;
    private final BigDecimal price;

}
