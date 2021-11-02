package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice());
        Product savedProduct = productRepository.save(product);
        return new ProductResponse(savedProduct.getId(), savedProduct.getName(), savedProduct.getPrice());
    }

    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(), product.getPrice()))
                .collect(Collectors.toList());
    }
}
