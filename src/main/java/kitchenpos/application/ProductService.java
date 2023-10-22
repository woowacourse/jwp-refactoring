package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.ui.dto.ProductRequest;
import kitchenpos.ui.dto.ProductResponse;
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
        final Product product = productRequest.convert();

        product.validatePrice();

        final Product savedProduct = productRepository.save(product);
        return ProductResponse.from(savedProduct);
    }

    public List<ProductResponse> list() {
        final List<Product> allProducts = productRepository.findAll();
        return allProducts.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
