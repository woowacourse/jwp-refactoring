package kitchenpos.product.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import kitchenpos.product.ui.request.ProductCreateRequest;

@Service
@Transactional(readOnly = true)
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
