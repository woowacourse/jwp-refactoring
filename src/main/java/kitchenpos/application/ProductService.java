package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequest;
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
    public Product create(ProductCreateRequest productCreateRequest) {
        final Long price = productCreateRequest.getPrice();

        if (Objects.isNull(price) || price < 0) {
            throw new IllegalArgumentException();
        }

        return productDao.save(new Product(productCreateRequest.getName(), price));
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
