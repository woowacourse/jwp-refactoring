package kitchenpos.core.product.application;

import java.util.List;
import kitchenpos.core.product.domain.ProductDao;
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
    public Product create(final Product productRequest) {
        return productDao.save(productRequest);
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
