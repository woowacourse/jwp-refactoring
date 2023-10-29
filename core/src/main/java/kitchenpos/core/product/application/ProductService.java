package kitchenpos.core.product.application;

import java.util.List;
import kitchenpos.core.product.application.dto.ProductResponse;
import kitchenpos.core.product.domain.Name;
import kitchenpos.core.product.domain.Price;
import kitchenpos.core.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final Name name, final Price price) {
        final Product product = new Product(null, name, price);
        return ProductResponse.from(productDao.save(product));
    }

    public List<ProductResponse> list() {
        return ProductResponse.from(productDao.findAll());
    }
}
