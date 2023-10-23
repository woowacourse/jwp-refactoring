package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository implements ProductDao {

    private final JdbcTemplateProductDao jdbcTemplateProductDao;

    public ProductRepository(final JdbcTemplateProductDao jdbcTemplateProductDao) {
        this.jdbcTemplateProductDao = jdbcTemplateProductDao;
    }

    @Override
    public Product save(final Product entity) {
        return jdbcTemplateProductDao.save(entity);
    }

    @Override
    public Optional<Product> findById(final Long id) {
        return jdbcTemplateProductDao.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplateProductDao.findAll();
    }
}
