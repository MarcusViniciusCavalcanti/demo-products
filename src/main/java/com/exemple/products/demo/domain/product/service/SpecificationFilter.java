package com.exemple.products.demo.domain.product.service;

import com.exemple.products.demo.domain.product.entity.Product;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpecificationFilter {

    private static final String WILD_CARD = "%";

    public static Specification<Product> buildFilter(String name, BigDecimal min, BigDecimal max) {
        return (root, query, builder) -> {
            query.distinct(true);
            var predicate = builder.conjunction();

            if (Objects.nonNull(name)) {
                predicate.getExpressions().add(
                    builder.or(
                        builder.like(
                            builder.lower(root.get("name")),
                            builder.lower(builder.literal(concatenateKeyValueWithWildCard(name)))
                        )
                    )
                );
            }

            if (Objects.nonNull(min)) {
                predicate.getExpressions().add(
                    builder.greaterThanOrEqualTo(root.get("price"), min));
            }

            if (Objects.nonNull(max)) {
                predicate.getExpressions().add(
                    builder.lessThanOrEqualTo(root.get("price"), max));
            }

            return predicate;
        };
    }

    private static String concatenateKeyValueWithWildCard(String value) {
        return WILD_CARD + value.toLowerCase(Locale.getDefault()) + WILD_CARD;
    }
}
