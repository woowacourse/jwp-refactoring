package kitchenpos.application;

import java.util.List;
import kitchenpos.repository.menu.MenuRepository;
import kitchenpos.repository.menu.ProductRepository;
import kitchenpos.domain.menu.Product;
import kitchenpos.dto.request.ProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final MenuRepository menuRepository;

    public ProductService(ProductRepository productRepository, MenuRepository menuRepository) {
        this.productRepository = productRepository;
        this.menuRepository = menuRepository;
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
