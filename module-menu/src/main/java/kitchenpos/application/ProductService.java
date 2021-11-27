package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.dto.request.ProductRequestDto;
import kitchenpos.dto.response.ProductResponseDto;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponseDto create(final ProductRequestDto productRequestDto) {
        final Product product = new Product(productRequestDto.getName(),
            productRequestDto.getPrice());
        final Product created = productRepository.save(product);

        return new ProductResponseDto(created.getId(), created.getName(), created.getPrice());
    }

    public List<ProductResponseDto> list() {
        final List<Product> products = productRepository.findAll();

        return products.stream()
            .map(product -> new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice()
            )).collect(toList());
    }
}
