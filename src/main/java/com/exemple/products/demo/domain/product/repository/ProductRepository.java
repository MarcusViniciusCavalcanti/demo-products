package com.exemple.products.demo.domain.product.repository;

import com.exemple.products.demo.domain.product.entity.Product;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Boolean existsProductByName(String name);
}
