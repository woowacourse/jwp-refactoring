package kitchenpos.domain.product.service;

import kitchenpos.domain.common.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.repository.ProductRepository;
import kitchenpos.domain.product.service.dto.ProductCreateRequest;
import kitchenpos.domain.product.service.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductCreateRequest request) {
        final BigDecimal price = BigDecimal.valueOf(request.getPrice());
        final Product product = new Product(request.getName(), new Price(price));
        final Product savedProduct = productRepository.save(product);

        return ProductResponse.toDto(savedProduct);
    }

    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
                .map(ProductResponse::toDto)
                .collect(toList());
    }
}
