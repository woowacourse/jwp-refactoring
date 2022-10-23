package kitchenpos.application;

import static kitchenpos.application.exception.ExceptionType.INVALID_PRODUCT_PRICE_EXCEPTION;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.application.exception.CustomIllegalArgumentException;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(final Product product) {
        final BigDecimal price = product.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new CustomIllegalArgumentException(INVALID_PRODUCT_PRICE_EXCEPTION);
        }

        return productDao.save(product);
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
