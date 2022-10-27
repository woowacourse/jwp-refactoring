package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductDao productDao;
    private final MenuDao menuDao;

    public ProductService(final ProductDao productDao, final MenuDao menuDao) {
        this.productDao = productDao;
        this.menuDao = menuDao;
    }

    @Transactional
    public Product create(final ProductRequest productRequest) {
        final Product product = productRequest.toDomain();

        return productDao.save(product);
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
