package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.ProductCreateRequest;
import kitchenpos.application.dto.ProductResponse;
import kitchenpos.dao.ProductDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductCreateRequest productCreateRequest) {
        return ProductResponse.from(productDao.save(productCreateRequest.toEntity()));
    }

    public List<ProductResponse> list() {
        return ProductResponse.listOf(productDao.findAll());
    }
}
