package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product.Product;
import kitchenpos.ui.request.ProductCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(final ProductCreateRequest request) {
        Product product = new Product(request.getName(), request.getPrice());
        return productDao.save(product);
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
