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
        final String name = product.getName();
        final BigDecimal price = product.getPrice();

        if (Objects.isNull(name) || name.length() > 255) {
            throw new IllegalArgumentException();
        }

        if (Objects.isNull(price)
                || price.compareTo(BigDecimal.ZERO) < 0
                || price.compareTo(BigDecimal.valueOf(Math.pow(10, 17))) >= 0) {
            throw new IllegalArgumentException();
        }

        return productDao.save(product);
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
