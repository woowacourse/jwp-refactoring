package kitchenpos.domain.menu.service;

import kitchenpos.domain.common.Price;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.menu.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final Product product) {
        final Price price = product.getPrice();

        if (Objects.isNull(price) || price.isUnderZero()) {
            throw new IllegalArgumentException();
        }

        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
