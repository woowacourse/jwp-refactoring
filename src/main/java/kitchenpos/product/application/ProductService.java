package kitchenpos.product.application;

import java.util.List;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.request.ProductCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
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
