package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ProductDto;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductDto create(final String productName, final BigDecimal price) {
        final Product product = Product.create(productName, price);
        return ProductDto.from(productRepository.save(product));
    }

    public List<ProductDto> list() {
        return productRepository.findAll()
                .stream()
                .map(ProductDto::from)
                .collect(Collectors.toList());
    }
}
