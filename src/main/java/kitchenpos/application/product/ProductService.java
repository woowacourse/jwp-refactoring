package kitchenpos.application.product;

import kitchenpos.application.product.request.ProductCreateRequest;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.product.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(ProductCreateRequest request) {
        final BigDecimal price = request.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
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
