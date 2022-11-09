package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public ProductResponse create(Product request) {
        Product savedProduct = productDao.save(request);
        return new ProductResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productDao.findAll()
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }
}
