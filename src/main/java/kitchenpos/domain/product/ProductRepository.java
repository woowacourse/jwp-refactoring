package kitchenpos.domain.product;

import java.util.List;
import kitchenpos.dao.ProductDao;
import org.springframework.stereotype.Component;

@Component
public class ProductRepository {

    private final ProductDao productDao;

    public ProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

    public Product save(Product product) {
        return productDao.save(product);
    }

    public List<Product> findAll() {
        return productDao.findAll();
    }

    public Product findById(Long id) {
        return productDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Product> findAllByIdIn(List<Long> ids) {
        return productDao.findAllByIdIn(ids);
    }
}
