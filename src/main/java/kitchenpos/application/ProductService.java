package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.ProductCreateRequest;
import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductCreateRequest request) {
        final Product product = Product.forSave(request.getName(), request.getPrice());

        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
