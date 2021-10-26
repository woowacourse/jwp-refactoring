package kitchenpos.application;

import kitchenpos.application.dtos.ProductRequest;
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
    public Product create(final ProductRequest request) {
        final Long price = request.getPrice();

        if (Objects.isNull(price) || price.compareTo(0L) < 0) {
            throw new IllegalArgumentException();
        }

        final Product product = Product.builder()
                .name(request.getName())
                .price(BigDecimal.valueOf(request.getPrice()))
                .build();

        return productDao.save(product);
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
