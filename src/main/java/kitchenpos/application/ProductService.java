package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productDao;

    public ProductService(final ProductRepository productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(final Product product) {
        final Price price = product.getPrice();

        if (Objects.isNull(price) || price.getValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        return productDao.save(product);
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
