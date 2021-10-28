package kitchenpos.application;

import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.ui.dto.ProductRequest;
import kitchenpos.ui.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        return ProductResponse.from(productRepository.save(productRequest.toProduct()));
    }

    public List<ProductResponse> list() {
        return ProductResponse.from(productRepository.findAll());
    }
}
