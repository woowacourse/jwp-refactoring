package kitchenpos.product.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductDao;
import kitchenpos.product.dto.ProductCreateRequest;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Long create(final ProductCreateRequest request) {
        Product product = request.toEntity();
        return productDao.save(product).getId();
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
