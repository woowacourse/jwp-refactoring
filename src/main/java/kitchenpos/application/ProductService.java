package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.jpa.JpaProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final JpaProductRepository productRepository;

    public ProductService(JpaProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductCreateRequest request) {
        return productRepository.save(request.toProduct());
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
