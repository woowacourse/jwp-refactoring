package kitchenpos.product.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.product.service.dto.ProductRequest;
import kitchenpos.product.service.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(final ProductRequest request) {
        return ProductResponse.of(productRepository.save(request.toEntity()));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }
}
