package kitchenpos.product.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.product.model.Product;
import kitchenpos.product.application.dto.ProductCreateRequestDto;
import kitchenpos.product.application.dto.ProductResponseDto;
import kitchenpos.product.repository.ProductRepository;

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

    @Transactional(readOnly = true)
    public List<ProductResponseDto> list() {
        List<Product> products = productRepository.findAll();
        return ProductResponseDto.listOf(products);
    }
}
