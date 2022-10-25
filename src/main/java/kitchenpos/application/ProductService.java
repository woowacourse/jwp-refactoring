package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(ProductRequest productRequest) {
        BigDecimal price = productRequest.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        }

        Product product = productDao.save(productRequest.toEntity());
        return ProductResponse.from(product);
    }

    public List<ProductResponse> list() {
        return productDao.findAll()
            .stream()
            .map(ProductResponse::from)
            .collect(Collectors.toList());
    }
}
