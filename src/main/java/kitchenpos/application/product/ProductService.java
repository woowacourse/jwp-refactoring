package kitchenpos.application.product;

import kitchenpos.application.product.dto.ProductCreateRequest;
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
    public Product create(final ProductCreateRequest req) {
        Product updateProduct = req.toDomain();
        updateProduct.validatePriceIsEmpty();

        return productDao.save(updateProduct);
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
