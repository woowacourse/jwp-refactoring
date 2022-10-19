package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(final Product product) {
        validateName(product.getName());
        validatePrice(product.getPrice());

        return productDao.save(product);
    }

    private void validateName(final String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("유효하지 않은 상품명 :" + name);
        }
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || isLessThanZero(price)) {
            throw new IllegalArgumentException("유효하지 않은 가격 : " + price);
        }
    }

    private boolean isLessThanZero(final BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) < 0;
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
