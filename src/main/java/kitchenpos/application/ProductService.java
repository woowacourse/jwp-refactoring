package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.application.dto.product.ProductCreateRequest;
import kitchenpos.application.dto.product.ProductCreateResponse;
import kitchenpos.application.dto.product.ProductResponse;
import kitchenpos.domain.Product;
import kitchenpos.persistence.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductCreateResponse create(final ProductCreateRequest request) {
        final BigDecimal price = request.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        final Product product = new Product(request.getName(), request.getPrice());
        final Product savedProduct = productRepository.save(product);

        return ProductCreateResponse.of(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        final List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::of)
                .collect(toList());
    }
}
