package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.product.request.ProductCreateRequest;
import kitchenpos.dto.product.response.ProductResponse;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Long create(final ProductCreateRequest request) {
        final Product product = request.toEntity();
        return productRepository.save(product).id();
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        final List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }
}
