package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ProductDto;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductDto create(final ProductDto product) {
        final Product savedProduct = productRepository.save(product.toEntity());
        return ProductDto.from(savedProduct);
    }

    public List<ProductDto> list() {
        return productRepository.findAll()
            .stream()
            .map(ProductDto::from)
            .collect(Collectors.toList());
    }
}
