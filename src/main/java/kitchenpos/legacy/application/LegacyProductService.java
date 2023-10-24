package kitchenpos.legacy.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.legacy.dao.ProductDao;
import kitchenpos.legacy.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LegacyProductService {

    private final ProductDao productDao;

    public LegacyProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public Product create(Product product) {
        BigDecimal price = product.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        return productDao.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productDao.findAll();
    }
}
