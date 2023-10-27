package application;

import domain.Product;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.ProductDao;
import ui.request.ProductCreateRequest;
import ui.response.ProductResponse;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductCreateRequest request) {
        final var product = new Product(request.getName(), request.getPrice());
        return ProductResponse.from(productDao.save(product));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productDao.findAll().stream()
                         .map(ProductResponse::from)
                         .collect(Collectors.toList());
    }
}
