package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.application.dto.request.CreateProductDto;
import kitchenpos.application.dto.response.ProductDto;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductDto create(final CreateProductDto createProductDto) {
        final Product product = createProductDto.toEntity();
        return ProductDto.of(productRepository.save(product));
    }

    public List<ProductDto> list() {
        return productRepository.findAll()
                .stream()
                .map(ProductDto::of)
                .collect(Collectors.toList());
    }
}
