package kitchenpos.product.application;

import java.util.List;
import kitchenpos.product.application.request.ProductCreateRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductPrice;
import kitchenpos.product.domain.ProductRepository;
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
        final Product product = new Product(request.getName(), new ProductPrice(request.getPrice()));
        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
