package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.ProductRequest;
import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductRequest productRequest) {
        return productRepository.save(productRequest.toEntity());
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
