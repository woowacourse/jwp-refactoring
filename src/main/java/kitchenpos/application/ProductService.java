package kitchenpos.application;

import java.util.List;
import kitchenpos.application.response.ProductResponse;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.domain.product.Name;
import kitchenpos.domain.product.Price;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final Name name, final Price price) {
        final Product product = new Product(null, name, price);
        return ProductResponse.from(productDao.save(product));
    }

    public List<ProductResponse> list() {
        return ProductResponse.from(productDao.findAll());
    }
}
