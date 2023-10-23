package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequest;
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
    public ProductResponse create(final ProductCreateRequest request) {
        final Product product = saveProduct(request);
        return ProductResponse.toResponse(product);
    }

    private Product saveProduct(final ProductCreateRequest request) {
        return productDao.save(
                new Product(
                        request.getName(),
                        request.getPrice()
                )
        );
    }

    public List<ProductResponse> list() {
        final List<Product> products = productDao.findAll();
        return products.stream()
                .map(ProductResponse::toResponse)
                .collect(Collectors.toList());
    }
}
