package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.menu.Product;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        final Product product = request.toEntity();
        final Product savedProduct = productDao.save(product);
        return ProductResponse.from(savedProduct);
    }

    public List<ProductResponse> list() {
        final List<Product> products = productDao.findAll();
        return ProductResponse.from(products);
    }
}
