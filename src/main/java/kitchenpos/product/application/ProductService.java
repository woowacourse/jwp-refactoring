package kitchenpos.product.application;

import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.product.ui.request.ProductRequest;
import kitchenpos.product.ui.response.ProductResponse;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(final ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(), productRequest.getPrice());
        final Product savedProduct = productRepository.save(product);
        return ProductResponse.from(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        final List<Product> allProducts = productRepository.findAll();
        return allProducts.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
