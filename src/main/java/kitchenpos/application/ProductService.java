package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.application.dto.ProductRequest;
import kitchenpos.application.dto.ProductResponse;
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
    public ProductResponse create(final ProductRequest request) {
        final BigDecimal price = request.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        return ProductResponse.of(productDao.save(request.toEntity()));
    }

    public List<ProductResponse> list() {
        return productDao.findAll().stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }
}
