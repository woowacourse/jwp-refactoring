package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.ui.dto.product.ProductRequest;
import kitchenpos.ui.dto.product.ProductResponse;
import kitchenpos.ui.dto.product.ProductResponses;
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
        return ProductResponse.from(productDao.save(request.toEntity()));
    }

    public ProductResponses list() {
        return ProductResponses.from(productDao.findAll());
    }
}
