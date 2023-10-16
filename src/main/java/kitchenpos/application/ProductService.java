package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public Product create(final ProductCreateRequest request) {
        final Product product = Product.of(request.getName(), request.getPrice());
        return productDao.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> readAll() {
        return productDao.findAll();
    }
}
