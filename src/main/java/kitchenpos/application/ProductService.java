package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequest;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(final ProductCreateRequest request) {
        final Product product = new Product(request.getName(), request.getPrice());
        return productDao.save(product);
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}

