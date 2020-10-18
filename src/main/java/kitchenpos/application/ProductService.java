package kitchenpos.application;

import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.ui.dto.product.ProductRequest;
import kitchenpos.ui.dto.product.ProductResponse;
import kitchenpos.ui.dto.product.ProductResponses;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        return ProductResponse.from(productRepository.save(request.toEntity()));
    }

    public ProductResponses list() {
        return ProductResponses.from(productRepository.findAll());
    }
}
