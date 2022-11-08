package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.application.dto.ProductRequest;
import kitchenpos.product.application.dto.ProductResponse;
import kitchenpos.product.domain.repository.ProductDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        return new ProductResponse(productDao.save(request.toProduct()));
    }

    public List<ProductResponse> list() {
        return productDao.findAll().stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }
}
