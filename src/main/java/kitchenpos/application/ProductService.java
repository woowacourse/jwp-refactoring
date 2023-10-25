package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.request.ProductRequest;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        final Product product = new Product(request.getName(), request.getPrice());
        return ProductResponse.from(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return ProductResponse.from(productRepository.findAll());
    }
}
