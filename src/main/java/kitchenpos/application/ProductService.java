package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final MenuDao menuDao;

    public ProductService(final ProductRepository productRepository, final MenuDao menuDao) {
        this.productRepository = productRepository;
        this.menuDao = menuDao;
    }

    @Transactional
    public Product create(final ProductRequest productRequest) {
        final Product product = productRequest.toDomain();

        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
