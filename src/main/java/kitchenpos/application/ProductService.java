package kitchenpos.application;

import kitchenpos.application.dto.ProductCreateRequest;
import kitchenpos.application.dto.ProductResponse;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductCreateRequest request) {
        Product product = new Product(request.getName(), request.getPrice());
        Product save = productDao.save(product);
        return ProductResponse.of(save);
    }

    public List<ProductResponse> list() {
        return ProductResponse.list(productDao.findAll());
    }
}
