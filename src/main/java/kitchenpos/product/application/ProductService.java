package kitchenpos.product.application;

import kitchenpos.product.application.dto.request.ProductCreateRequest;
import kitchenpos.product.application.dto.response.ProductResponse;
import kitchenpos.product.application.mapper.ProductMapper;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductCreateRequest productCreateRequest) {
        final Product product = ProductMapper.mapToProduct(productCreateRequest);
        final Product savedProduct = productRepository.save(product);
        return ProductMapper.mapToResponse(savedProduct);
    }

    public List<ProductResponse> list() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}
