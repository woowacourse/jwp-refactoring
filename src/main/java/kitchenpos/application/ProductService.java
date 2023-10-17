package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.ProductCreateRequest;
import kitchenpos.ui.dto.response.ProductCreateResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductCreateResponse create(final ProductCreateRequest nameAndPrice) {
        final Product product = new Product(null, nameAndPrice.getName(), nameAndPrice.getPrice());
        return ProductCreateResponse.from(productDao.save(product));
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
