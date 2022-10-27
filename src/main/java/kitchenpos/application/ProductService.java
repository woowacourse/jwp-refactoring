package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }


    public Product create(final Product request) {
        return productDao.save(request);
    }

    @Transactional(readOnly = true)
    public List<Product> list() {
        return productDao.findAll();
    }
}
