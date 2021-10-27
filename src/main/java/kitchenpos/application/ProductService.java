package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.request.CreateProductRequest;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final CreateProductRequest request) {
        final Product product = request.toEntity();
        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
