package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        final BigDecimal price = request.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        Product product = Product.builder()
            .name(request.getName())
            .price(request.getPrice())
            .build();

        Product savedProduct = productDao.save(product);
        return ProductResponse.from(savedProduct);
    }

    public List<ProductResponse> list() {
        List<Product> products = productDao.findAll();
        return ProductResponse.listFrom(products);
    }
}
