package kitchenpos.product.domain;

import java.util.List;
import java.util.Optional;
import kitchenpos.product.infra.JdbcTemplateProductDao;
import kitchenpos.product.infra.ProductDao;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository implements ProductDao {

    private final JdbcTemplateProductDao productDao;

    public ProductRepository(final JdbcTemplateProductDao jdbcTemplateProductDao) {
        this.productDao = jdbcTemplateProductDao;
    }

    @Override
    public Product save(final Product entity) {
        return productDao.save(entity);
    }

    @Override
    public Optional<Product> findById(final Long id) {
        return productDao.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return productDao.findAll();
    }
}
