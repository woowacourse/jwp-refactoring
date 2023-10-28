package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.application.dto.ProductDto;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductDto create(final ProductDto productDto) {
        final Product savedProduct = productRepository.save(productDto.toEntity());
        return ProductDto.from(savedProduct);
    }

    public List<ProductDto> list() {
        return productRepository.findAll()
            .stream()
            .map(ProductDto::from)
            .collect(Collectors.toList());
    }
}
