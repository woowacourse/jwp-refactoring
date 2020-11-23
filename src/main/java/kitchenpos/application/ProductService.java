package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
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
        Product product = productDao.save(request.toEntity());

        return ProductResponse.of(product);
    }

    public List<ProductResponse> list() {
        return productDao.findAll().stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }
}
