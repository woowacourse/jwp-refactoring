package kitchenpos.application.product;

import kitchenpos.application.product.request.ProductCreateRequest;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(ProductCreateRequest request) {
        Product product = mapToProduct(request);
        return productRepository.save(product);
    }

    private Product mapToProduct(ProductCreateRequest request) {
        return Product.of(request.getName(), request.getPrice());
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
