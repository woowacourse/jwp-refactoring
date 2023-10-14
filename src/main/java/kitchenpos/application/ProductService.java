package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.vo.product.ProductRequest;
import kitchenpos.vo.product.ProductResponse;
import kitchenpos.vo.product.ProductsResponse;
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

        Product product = new Product.Builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .build();

        Product savedProduct = productDao.save(product);

        return ProductResponse.of(savedProduct);
    }

    public ProductsResponse list() {
        return ProductsResponse.of(productDao.findAll());
    }
}
