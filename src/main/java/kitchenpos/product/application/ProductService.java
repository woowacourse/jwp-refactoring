package kitchenpos.product.application;

import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(String name, BigDecimal price) {
        Product product = new Product(name, price);
        return productDao.save(product);
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
