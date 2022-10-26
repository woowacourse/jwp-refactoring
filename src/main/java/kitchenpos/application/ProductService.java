package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.dto.response.ProductsResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductCreateRequest request) {
        Product product = new Product(request.getName(), request.getPrice());
        product.validateCorrectPrice();
        Product savedProduct = productDao.save(product);
        return ProductResponse.of(savedProduct);
    }

    public ProductsResponse list() {
        List<Product> products = productDao.findAll();
        return ProductsResponse.of(products);
    }
}
