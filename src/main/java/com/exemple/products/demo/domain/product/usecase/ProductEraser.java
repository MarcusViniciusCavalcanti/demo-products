package com.exemple.products.demo.domain.product.usecase;

import com.exemple.products.demo.domain.product.repository.ProductRepository;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductEraser {

    private final ProductRepository productRepository;

    @Transactional
    public void delete(UUID uuid) {
        var product = productRepository.findById(uuid).orElseThrow(EntityNotFoundException::new);
        productRepository.delete(product);
    }
}
