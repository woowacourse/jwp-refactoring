package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.ProductResponse;
import org.springframework.stereotype.Repository;

public interface ProductDao {
    Product save(Product entity);

    Optional<Product> findById(Long id);

    List<ProductResponse> findAll();
}

@Repository
class ProductRepository implements ProductDao {

    private JdbcTemplateProductDao productDao;

    public ProductRepository(final JdbcTemplateProductDao productDao) {
        this.productDao = productDao;
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
    public List<ProductResponse> findAll() {
        final List<Product> products = productDao.findAll();
        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
