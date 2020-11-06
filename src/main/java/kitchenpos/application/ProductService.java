package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.ProductRequest;
import kitchenpos.application.dto.ProductResponse;
import kitchenpos.domain.entity.Product;
import kitchenpos.domain.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        Product product = request.toEntity();
        Product saved = productRepository.save(product);
        return ProductResponse.of(saved);
    }

    public List<ProductResponse> list() {
        return ProductResponse.listOf(productRepository.findAll());
    }
}
