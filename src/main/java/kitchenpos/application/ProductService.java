package kitchenpos.application;

import java.util.List;
import kitchenpos.repository.ProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.CreateProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productDao;

    public ProductService(final ProductRepository productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(final CreateProductRequest request) {
        return productDao.save(new Product(request.getName(), request.getPrice()));
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
