package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.application.dto.product.ProductCreateRequest;
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
        return productDao.save(createProductByRequest(request));
    }

    private Product createProductByRequest(final ProductCreateRequest request) {
        return new Product(request.getName(), request.getPrice());
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
