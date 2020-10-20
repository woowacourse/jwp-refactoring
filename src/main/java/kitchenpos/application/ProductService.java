package kitchenpos.application;

import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Price.of(productRequest.getPrice());

        Product product = productRequest.to();
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}
