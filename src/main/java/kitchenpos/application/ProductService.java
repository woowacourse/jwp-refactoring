package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ProductRequest;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(ProductRequest productRequest) {
        final BigDecimal price = productRequest.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        return productDao.save(productRequest.toEntity());
    }

    public List<ProductResponse> list() {
        List<Product> products = productDao.findAll();
        return products.stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(), product.getPrice()))
                .collect(Collectors.toList());
    }
}
