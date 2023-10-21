package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice());
        return ProductResponse.toResponse(productRepository.save(product));
    }

    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
                .map(ProductResponse::toResponse)
                .collect(Collectors.toList());
    }
}
