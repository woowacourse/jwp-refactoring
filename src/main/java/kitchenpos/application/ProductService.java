package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
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
    public ProductResponse create(final ProductRequest productRequest) {
        final Product product = productRequest.toEntity();
        final Product savedProduct = productDao.save(product);
        return ProductResponse.of(savedProduct);
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
