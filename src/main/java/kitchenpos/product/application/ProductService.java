package kitchenpos.product.application;

import java.util.List;
import kitchenpos.product.repository.ProductDao;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(final Product product) {
        product.validateProductPrice();
        return productDao.save(product);
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
