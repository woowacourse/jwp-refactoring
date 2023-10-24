package kitchenpos.domain.product;

import kitchenpos.dto.request.CreateProductRequest;
import kitchenpos.dto.response.CreateProductResponse;
import kitchenpos.dto.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public CreateProductResponse create(final CreateProductRequest request) {
        Product product = ProductMapper.toProduct(request);
        return CreateProductResponse.from(productRepository.save(product));
    }

    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
