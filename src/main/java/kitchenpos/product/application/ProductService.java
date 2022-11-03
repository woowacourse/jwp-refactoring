package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.ui.request.ProductCreateRequest;
import kitchenpos.product.response.ProductResponse;
import kitchenpos.product.repository.ProductDao;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductCreateRequest request) {
        final Product product = productDao.save(new Product(request.getName(), request.getPrice()));
        return ProductResponse.from(product);
    }

    public List<ProductResponse> list() {
        return productDao.findAll().
                stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    public long countProductInIds(final List<Long> productIds) {
        return productDao.countProductInIds(productIds);
    }
}
