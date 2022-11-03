package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.dto.response.ProductsResponse;
import kitchenpos.dao.ProductDao;
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
        Product product = request.toEntity();
        productDao.save(product);
        return ProductResponse.from(product);
    }

    public ProductsResponse list() {
        List<Product> products = productDao.findAll();
        return ProductsResponse.from(products);
    }
}
