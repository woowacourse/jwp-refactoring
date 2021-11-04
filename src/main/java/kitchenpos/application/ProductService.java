package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(final ProductRequest productRequest) {
        if (isInvalidPrice(productRequest.getPrice())) {
            throw new IllegalArgumentException();
        }
        return productDao.save(new Product(productRequest.getName(), productRequest.getPrice()));
    }

    private boolean isInvalidPrice(BigDecimal price) {
        return Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0;
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
