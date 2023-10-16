package kitchenpos.application;

import java.util.List;
import kitchenpos.application.request.ProductCreateRequest;
import kitchenpos.domain.Product;
import kitchenpos.persistence.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(ProductCreateRequest request) {
        return productRepository.save(request.toEntity());
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
