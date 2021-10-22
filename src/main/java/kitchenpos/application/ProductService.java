package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(final Product product) {
        validateName(product);
        validatePrice(product);

        return productDao.save(product);
    }

    private void validateName(Product product) {
        final String name = product.getName();

        if (Objects.isNull(name) || name.length() < 1) {
            throw new IllegalArgumentException();
        }
    }

    private void validatePrice(Product product) {
        final BigDecimal price = product.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
