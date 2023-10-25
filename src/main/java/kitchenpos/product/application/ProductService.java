package kitchenpos.product.application;

import java.util.List;
import kitchenpos.product.Product;
import kitchenpos.product.persistence.ProductRepository;
import kitchenpos.product.request.ProductCreateRequest;
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
        return productRepository.save(new Product(request.getName(), request.getPrice()));
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
