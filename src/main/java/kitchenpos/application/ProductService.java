package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequestDto;
import kitchenpos.dto.ProductResponseDto;
import kitchenpos.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponseDto create(final ProductCreateRequestDto productCreateRequestDto) {
        Product product = productCreateRequestDto.toEntity();
        Product saved = productRepository.save(product);
        return ProductResponseDto.from(saved);
    }

    public List<ProductResponseDto> list() {
        List<Product> products = productRepository.findAll();
        return ProductResponseDto.listOf(products);
    }
}
