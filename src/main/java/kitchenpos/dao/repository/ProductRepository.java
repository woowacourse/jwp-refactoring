package kitchenpos.dao.repository;

import java.util.List;
import kitchenpos.dao.jdbctemplate.JdbcTemplateProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository implements ProductDao {

    private final JdbcTemplateProductDao productDao;

    public ProductRepository(JdbcTemplateProductDao jdbcTemplateProductDao) {
        this.productDao = jdbcTemplateProductDao;
    }


    @Override
    public Product save(Product entity) {
        return productDao.save(entity);
    }

    @Override
    public Product findById(Long id) {
        return productDao.findById(id)
                .orElseThrow(() -> new InvalidDataAccessApiUsageException("상품은 DB에 등록되어야 한다"));
    }

    @Override
    public List<Product> findAll() {
        return productDao.findAll();
    }
}
