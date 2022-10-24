package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.ProductRequest;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(final ProductRequest request) {
        return productDao.save(request.toProduct());
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
