package com.exemple.products.demo.structure.dto.inputs;

import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class InputProduct {

    @NotBlank(message = "is required")
    private final String name;

    @NotBlank(message = "is required")
    private final String description;

    @NotNull(message = "is required")
    @Positive(message = "must be greater than zero")
    private final BigDecimal price;

}
