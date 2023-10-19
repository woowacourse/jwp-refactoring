package kitchenpos.application;

import kitchenpos.dao.JpaProductRepository;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    private final JpaProductRepository jpaProductRepository;

    public ProductService(final JpaProductRepository JpaProductRepository) {
        this.jpaProductRepository = JpaProductRepository;
    }

    @Transactional
    public Product create(final Product product) {
        final BigDecimal price = product.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        return jpaProductRepository.save(product);
    }

    public List<Product> list() {
        return jpaProductRepository.findAll();
    }
}
