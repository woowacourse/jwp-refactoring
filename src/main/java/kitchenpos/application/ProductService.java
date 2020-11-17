package kitchenpos.application;

import kitchenpos.domain.menu.Product;
import kitchenpos.domain.menu.ProductRepository;
import kitchenpos.dto.menu.ProductCreateRequest;
import kitchenpos.dto.menu.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse create(final ProductCreateRequest request) {
        validate(request);
        Product product = request.toProduct();
        Product savedProduct = productRepository.save(product);

        return new ProductResponse(savedProduct);
    }

    private void validate(final ProductCreateRequest request) {
        final String name = request.getName();
        final BigDecimal price = request.getPrice();

        if(Objects.isNull(name) || name.isEmpty()) {
            throw new IllegalArgumentException("잘못된 Product 이름이 입력되었습니다.");
        }
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("잘못된 Product 가격이 입력되었습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }
}
