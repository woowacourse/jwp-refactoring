package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.ProductCreateResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductCreateResponse create(final ProductCreateRequest productCreateRequest) {
        Product product = new Product.Builder()
                .name(productCreateRequest.getName())
                .price(productCreateRequest.getPrice())
                .build();
        Product savedProduct = productDao.save(product);
        return ProductCreateResponse.from(savedProduct);
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
