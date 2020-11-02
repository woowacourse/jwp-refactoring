package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.ProductRequest;
import kitchenpos.ui.dto.ProductResponse;
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
        Product product = productRequest.toProduct();
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    public List<ProductResponse> list() {
        List<Product> foundProducts = productRepository.findAll();

        return ProductResponse.listOf(foundProducts);
    }
}
