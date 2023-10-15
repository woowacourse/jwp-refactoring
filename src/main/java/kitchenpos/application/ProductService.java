package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductName;
import kitchenpos.domain.product.ProductPrice;
import kitchenpos.ui.dto.ProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public Product create(final ProductRequest request) {
        final BigDecimal price = request.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        return productDao.save(
                new Product(new ProductName(request.getName()), new ProductPrice(request.getPrice()))
        );
    }

    @Transactional(readOnly = true)
    public List<Product> list() {
        return productDao.findAll();
    }
}
