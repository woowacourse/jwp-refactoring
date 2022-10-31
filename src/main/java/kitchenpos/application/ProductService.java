package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateResponse;
import kitchenpos.dto.ProductFindResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductCreateResponse create(final String name, final BigDecimal price) {
        final Product product = productDao.save(new Product(name, price));
        return new ProductCreateResponse(product.getId(), product.getName(), product.getPrice());
    }

    public List<ProductFindResponse> list() {
        final List<Product> products = productDao.findAll();
        return products.stream()
                .map(ProductFindResponse::from)
                .collect(Collectors.toList());
    }
}
