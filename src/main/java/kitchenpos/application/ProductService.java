package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.command.CreateProductCommand;
import kitchenpos.application.response.ProductResponse;
import kitchenpos.domain.model.product.Product;
import kitchenpos.domain.model.product.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final CreateProductCommand command) {
        Product product = command.toEntity();
        Product saved = productRepository.save(product);
        return ProductResponse.of(saved);
    }

    public List<ProductResponse> list() {
        return ProductResponse.listOf(productRepository.findAll());
    }
}
