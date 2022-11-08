package kitchenpos.product.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.product.application.request.ProductCreateRequest;
import kitchenpos.product.application.response.ProductResponse;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductCreateRequest request) {
        Product product = productDao.save(new Product(request.getName(), request.getPrice()));
        return ProductResponse.from(product);
    }

    public List<ProductResponse> list() {
        return ProductResponse.from(productDao.findAll());
    }
}
