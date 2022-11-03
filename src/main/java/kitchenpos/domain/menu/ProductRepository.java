package kitchenpos.domain.menu;

import java.util.List;
import kitchenpos.dao.ProductDao;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final ProductDao productDao;

    public ProductRepository(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public Product get(final Long id) {
        return productDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    public Product add(final Product product) {
        return productDao.save(product);
    }

    public List<Product> getAll() {
        return productDao.findAll();
    }
}
