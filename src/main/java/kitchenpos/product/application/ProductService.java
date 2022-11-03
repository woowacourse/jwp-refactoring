package kitchenpos.product.application;

import java.util.stream.Collectors;
import kitchenpos.product.application.dto.ProductRequestDto;
import kitchenpos.product.application.dto.ProductResponse;
import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequestDto productRequestDto) {
        final Product savedProduct = productRepository.save(new Product(productRequestDto.getName(), productRequestDto.getPrice()));
        return ProductResponse.from(savedProduct);
    }

    public List<ProductResponse> list() {
        final List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
