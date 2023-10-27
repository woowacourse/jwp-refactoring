package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Product;
import kitchenpos.product.application.dto.ProductCreateDto;
import kitchenpos.product.application.dto.ProductDto;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.Price;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductDto create(final ProductCreateDto request) {
        final Product product = new Product(request.getName(), new Price(request.getPrice()));
        productRepository.save(product);

        return ProductDto.toDto(product);
    }

    public List<ProductDto> list() {
        final List<Product> products = productRepository.findAll();

        return products.stream()
                .map(ProductDto::toDto)
                .collect(Collectors.toList());
    }
}
