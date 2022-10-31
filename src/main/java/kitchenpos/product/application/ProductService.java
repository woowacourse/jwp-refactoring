package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.application.dto.ProductResponse;
import kitchenpos.product.application.dto.ProductSaveRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.dao.ProductDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public ProductResponse create(ProductSaveRequest request) {
        Product product = productDao.save(request.toEntity());
        return ProductResponse.toResponse(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productDao.findAll().stream()
            .map(ProductResponse::toResponse)
            .collect(Collectors.toUnmodifiableList());
    }
}
