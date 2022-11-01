package kitchenpos.application.product;

import java.util.stream.Collectors;
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
    public ProductResponse create(final ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(), productRequest.getPrice());

        return ProductResponse.from(productDao.save(product));
    }

    public List<ProductResponse> list() {
        final List<Product> products = productDao.findAll();

        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
