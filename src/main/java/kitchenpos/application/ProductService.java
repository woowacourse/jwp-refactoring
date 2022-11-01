package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(String name, Long price) {
        Product product = Product.of(name, price);
        return productDao.save(product);
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
