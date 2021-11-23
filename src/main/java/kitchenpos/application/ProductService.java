package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.application.dto.ProductDtoAssembler;
import kitchenpos.application.dto.request.ProductRequestDto;
import kitchenpos.application.dto.response.ProductResponseDto;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponseDto create(ProductRequestDto requestDto) {
        Product product = productRepository
            .save(new Product(requestDto.getName(), requestDto.getPrice()));

        return ProductDtoAssembler.productResponseDto(product);
    }

    public List<ProductResponseDto> list() {
        List<Product> products = productRepository.findAll();

        return products.stream()
            .map(ProductDtoAssembler::productResponseDto)
            .collect(toList());
    }
}
