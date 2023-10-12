package kitchenpos.application.product;

import kitchenpos.application.product.dto.ProductCreateRequest;
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
    public Product create(final ProductCreateRequest req) {
        final BigDecimal updatePrice = req.getPrice();

        if (Objects.isNull(updatePrice) || updatePrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        Product updateProduct = req.toDomain();
        return productDao.save(updateProduct);
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
