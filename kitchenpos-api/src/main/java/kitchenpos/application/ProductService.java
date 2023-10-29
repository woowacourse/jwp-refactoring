package kitchenpos.application;

import kitchenpos.application.dto.ProductRequest;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductRequest request) {
        Product product = request.toProduct();
        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
