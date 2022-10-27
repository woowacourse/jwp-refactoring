package kitchenpos.application;

import java.util.List;
import kitchenpos.application.request.ProductCreateRequest;
import kitchenpos.domain.product.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductCreateRequest request) {
        final Product product = new Product(request.getName(), new Price(request.getPrice()));
        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
