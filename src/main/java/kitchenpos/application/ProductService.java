package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductName;
import kitchenpos.domain.product.ProductPrice;
import kitchenpos.ui.dto.ProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public Product create(final ProductRequest request) {
        return productDao.save(
                new Product(new ProductName(request.getName()), new ProductPrice(request.getPrice()))
        );
    }

    @Transactional(readOnly = true)
    public List<Product> list() {
        return productDao.findAll();
    }
}
