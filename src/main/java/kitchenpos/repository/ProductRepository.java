package kitchenpos.repository;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
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
}
