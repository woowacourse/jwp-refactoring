package kitchenpos.infrastructure.menu;

import java.util.List;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.menu.ProductDao;
import kitchenpos.domain.menu.ProductRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductDao productDao;

    public ProductRepositoryImpl(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public Product get(final Long id) {
        return productDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public Product add(final Product product) {
        return productDao.save(product);
    }

    @Override
    public List<Product> getAll() {
        return productDao.findAll();
    }
}
