package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ProductResponse;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.ProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        Product savedProduct = productDao.save(request.toProduct());
        return new ProductResponse(savedProduct);
    }

    public List<ProductResponse> list() {
        return productDao.findAll()
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }
}
