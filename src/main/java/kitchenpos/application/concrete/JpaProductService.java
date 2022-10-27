package kitchenpos.application.concrete;

import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.exception.badrequest.ProductNameDuplicateException;
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
        validateDuplicateName(request.getName());
        final var newProduct = new Product(request.getName(), request.getPrice());

        return productRepository.save(newProduct);
    }

    private void validateDuplicateName(final String name) {
        if (productRepository.existsByName(name)) {
            throw new ProductNameDuplicateException(name);
        }
    }

    @Override
    public List<Product> list() {
        return productRepository.findAll();
    }
}
