package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.application.dto.ProductDto;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.product.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductDto create(final String productName, final BigDecimal price) {
        final Product product = Product.create(productName, price);

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        final Product savedProduct = productRepository.save(product);
        return ProductDto.from(savedProduct);
    }

    public List<ProductDto> list() {
        return productRepository.findAll()
                .stream()
                .map(ProductDto::from)
                .collect(Collectors.toList());
    }
}
