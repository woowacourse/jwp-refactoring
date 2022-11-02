package kitchenpos.product.application;

import java.util.List;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public Product create(final ProductCreateRequest request) {
        return productDao.save(new Product(request.getName(), request.getPrice()));
    }

    @Transactional(readOnly = true)
    public List<Product> list() {
        return productDao.findAll();
    }
}
