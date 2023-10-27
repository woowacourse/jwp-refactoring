package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.ProductCreateRequest;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
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
        Product product = new Product(request.getName(), request.getPrice());
        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
