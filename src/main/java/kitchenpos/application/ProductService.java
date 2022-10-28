package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ProductResponse;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final Product product) {
        Product savedProduct = productDao.save(product);
        return new ProductResponse(savedProduct);
    }

    public List<ProductResponse> list() {
        return productDao.findAll().stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }
}
