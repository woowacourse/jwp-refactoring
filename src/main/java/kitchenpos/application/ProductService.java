package kitchenpos.application;

import kitchenpos.domain.ProductRepository;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(Product product) {
        if (product.getName().length() > 255) {
            throw new IllegalArgumentException();
        }

        BigDecimal price = product.getPrice();

        if (Objects.isNull(price) ||
                (price.compareTo(BigDecimal.ZERO) < 0) ||
                (price.compareTo(BigDecimal.valueOf(Math.pow(10, 20))) >= 0)
        ) {
            throw new IllegalArgumentException();
        }

        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
