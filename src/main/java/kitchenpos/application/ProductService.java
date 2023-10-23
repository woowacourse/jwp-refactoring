package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.application.dto.product.ProductRequest;
import kitchenpos.application.dto.product.ProductResponse;
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
    public ProductResponse create(final ProductRequest productRequest) {
        final BigDecimal price = productRequest.getPrice();
        final Product product = new Product(productRequest.getName(), price);

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        final Product savedProduct = productDao.save(product);
        return ProductResponse.from(savedProduct);
    }

    public List<ProductResponse> list() {
        final List<Product> products = productDao.findAll();

        return products.stream()
            .map(ProductResponse::from)
            .collect(Collectors.toList());
    }
}
