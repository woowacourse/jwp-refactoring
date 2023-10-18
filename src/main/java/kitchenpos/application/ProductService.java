package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ProductCreateDto;
import kitchenpos.application.dto.ProductDto;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductDto create(final ProductCreateDto productCreateDto) {
        final Product newProduct = new Product(productCreateDto.getName(),
            productCreateDto.getPrice());

        final Product savedProduct = productRepository.save(newProduct);

        return ProductDto.from(savedProduct);
    }

    public List<ProductDto> list() {
        final List<Product> findProducts = productRepository.findAll();

        return findProducts.stream()
            .map(ProductDto::from)
            .collect(Collectors.toList());
    }
}
