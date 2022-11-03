package kitchenpos.application.concrete;

import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.domain.vo.Price;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.request.ProductCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class JpaProductService implements ProductService {
    private final ProductRepository productRepository;

    public JpaProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public Product create(final ProductCreateRequest request) {
        final var newProduct = new Product(request.getName(), Price.from(request.getPrice()));

        return productRepository.save(newProduct);
    }

    @Override
    public List<Product> list() {
        return productRepository.findAll();
    }
}
