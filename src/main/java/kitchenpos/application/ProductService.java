package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductCreateRequest request) {
        return productRepository.save(new Product(request.getName(), request.getPrice()));
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
