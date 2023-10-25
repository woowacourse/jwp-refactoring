package kitchenpos.product.application;

import kitchenpos.product.application.dto.CreateProductDto;
import kitchenpos.product.application.dto.ProductDto;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductName;
import kitchenpos.product.domain.ProductPrice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductDto create(CreateProductDto createProductDto) {
        Product product = new Product(
                new ProductName(createProductDto.getName()),
                new ProductPrice(createProductDto.getPrice()));

        Product savedProduct = productRepository.save(product);
        return new ProductDto(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getPrice());
    }

    @Transactional(readOnly = true)
    public List<ProductDto> list() {
        return productRepository.findAll()
                .stream()
                .map(product -> new ProductDto(
                        product.getId(),
                        product.getName(),
                        product.getPrice()
                ))
                .collect(Collectors.toList());
    }
}
