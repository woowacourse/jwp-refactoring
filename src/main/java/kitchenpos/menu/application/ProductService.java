package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.repository.ProductRepository;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.presentation.dto.request.ProductRequest;
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

        Product product = productRequest.toDomain();

        product.validate();

        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
