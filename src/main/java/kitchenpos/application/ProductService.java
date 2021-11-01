package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ProductRequest;
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
    public ProductResponse create(final ProductRequest request) {
        return ProductResponse.of(productRepository.save(request.toEntity()));
    }

    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }
}
