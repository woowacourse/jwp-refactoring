package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        final Product product = productRequest.toProduct();

        product.validatePrice();
        productDao.save(product);
        return ProductResponse.of(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        List<Product> products = productDao.findAll();
        return ProductResponse.listOf(products);
    }
}
