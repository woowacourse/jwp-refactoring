package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(final Product product) {
        final Product newProduct = new Product.Builder()
                .name(product.getName())
                .price(product.getPrice())
                .build();

        return productDao.save(newProduct);
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
