package kitchenpos.application.menu;

import java.util.stream.Collectors;
import kitchenpos.application.menu.dto.request.CreateProductDto;
import kitchenpos.application.menu.dto.response.ProductDto;
import kitchenpos.core.domain.menu.Product;
import kitchenpos.core.repository.menu.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductDto> list() {
        return productRepository.findAll()
                .stream()
                .map(ProductDto::of)
                .collect(Collectors.toList());
    }

    public ProductDto create(final CreateProductDto createProductDto) {
        final Product product = createProductDto.toEntity();
        return ProductDto.of(productRepository.save(product));
    }
}
