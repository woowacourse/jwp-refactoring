package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(ProductCreateRequest request) {
        Product product = new Product(request.getName(), BigDecimal.valueOf(request.getPrice()));
        Product savedProduct = productDao.save(product);
        return ProductResponse.from(savedProduct);
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
