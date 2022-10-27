package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductCreateRequest request) {
        final Product product = productDao.save(request.toEntity());

        return ProductResponse.from(product);
    }

    public List<ProductResponse> list() {
        final List<Product> products = productDao.findAll();

        return products.stream()
            .map(ProductResponse::from)
            .collect(Collectors.toList());
    }
}
