package kitchenpos.application;

import java.util.List;
import java.util.Objects;
import kitchenpos.dao.JpaProductRepository;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final JpaProductRepository jpaProductRepository;

    public ProductService(final JpaProductRepository JpaProductRepository) {
        this.jpaProductRepository = JpaProductRepository;
    }

    @Transactional
    public Product create(final Product product) {
        final Price price = product.getPrice();

        if (Objects.isNull(price)) {
            throw new IllegalArgumentException();
        }

        return jpaProductRepository.save(product);
    }

    public List<Product> list() {
        return jpaProductRepository.findAll();
    }
}
