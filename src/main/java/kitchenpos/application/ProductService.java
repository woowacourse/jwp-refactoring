package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.application.request.ProductRequest;
import kitchenpos.application.response.ProductResponse;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
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
        final Product product = new Product(request.getName(), request.getPrice());
        final BigDecimal price = product.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("올바르지 않은 상품 가격입니다.");
        }

        final Product savedProduct = productDao.save(product);

        return new ProductResponse(savedProduct);
    }

    public List<ProductResponse> list() {
        final List<Product> products = productDao.findAll();

        return products.stream()
                .map(ProductResponse::new)
                .collect(toList());
    }
}
