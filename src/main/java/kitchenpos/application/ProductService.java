package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(final ProductCreateRequest productCreateRequest) {
        String name = productCreateRequest.getName();
        BigDecimal price = new BigDecimal(productCreateRequest.getPrice());
        return productDao.save(new Product(name, price));
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
