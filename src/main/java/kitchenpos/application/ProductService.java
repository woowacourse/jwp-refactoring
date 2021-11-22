package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.product.ProductRequest;
import kitchenpos.application.dto.response.product.ProductResponse;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.repository.ProductRepository;
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
        Product product = productRequest.toEntity();

        Product savedProduct = productRepository.save(product);
        return ProductResponse.create(savedProduct);
    }

    public List<ProductResponse> list() {
        List<Product> list = productRepository.findAll();

        return list.stream()
                .map(ProductResponse::create)
                .collect(Collectors.toList());
    }
}
