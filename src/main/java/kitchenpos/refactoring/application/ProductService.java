package kitchenpos.refactoring.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.refactoring.application.dto.ProductRequest;
import kitchenpos.refactoring.application.dto.ProductResponse;
import kitchenpos.refactoring.domain.Product;
import kitchenpos.refactoring.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice());
        Product savedProduct = productRepository.save(product);

        return ProductResponse.from(savedProduct);
    }

    public List<ProductResponse> list() {

        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
