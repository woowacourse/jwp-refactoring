package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductCreateRequest request) {
        final BigDecimal price = request.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        final Product product = new Product(request.getName(), request.getPrice());
        final Product saved = productRepository.save(product);
        return ProductResponse.of(saved);
    }

    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
                .map(ProductResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }
}
