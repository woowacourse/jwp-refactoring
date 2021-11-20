package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.repository.ProductRepository;
import kitchenpos.menu.dto.request.ProductRequest;
import kitchenpos.menu.dto.response.ProductResponse;
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
        return ProductResponse.of(savedProduct);
    }

    public List<ProductResponse> list() {
        return ProductResponse.toList(productRepository.findAll());
    }
}
