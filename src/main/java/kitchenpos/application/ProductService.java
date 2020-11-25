package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.ProductCreateRequest;
import kitchenpos.application.dto.ProductResponse;
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
    public ProductResponse create(final ProductCreateRequest productCreateRequest) {
        return ProductResponse.from(productRepository.save(productCreateRequest.toEntity()));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return ProductResponse.listOf(productRepository.findAll());
    }
}
