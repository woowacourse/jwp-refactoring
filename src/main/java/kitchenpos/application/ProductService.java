package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(final ProductCreateRequest request) {
        final Product product = Product.of(request.getName(), request.getPrice());
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> readAll() {
        return productRepository.findAll();
    }
}
