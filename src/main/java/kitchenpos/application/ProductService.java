package kitchenpos.application;

import static java.util.stream.Collectors.*;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductResponse;
import kitchenpos.dto.ProductSaveRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductSaveRequest request) {
        Product product = productDao.save(new Product(request.getName(), request.getPrice()));
        return new ProductResponse(product);
    }

    public List<ProductResponse> list() {
        return productDao.findAll()
                .stream()
                .map(ProductResponse::new)
                .collect(toList());
    }
}
