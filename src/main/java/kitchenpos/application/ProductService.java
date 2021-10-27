package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.product.ProductRequestDto;
import kitchenpos.application.dto.response.product.ProductResponseDto;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponseDto create(ProductRequestDto productRequestDto) {
        Product product = new Product(productRequestDto.getName(), productRequestDto.getPrice());
        return ProductResponseDto.from(productRepository.save(product));
    }

    public List<ProductResponseDto> list() {
        return productRepository.findAll()
            .stream()
            .map(ProductResponseDto::from)
            .collect(Collectors.toList());
    }
}
