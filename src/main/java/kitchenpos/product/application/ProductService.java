package kitchenpos.product.application;

import kitchenpos.product.Product;
import kitchenpos.product.ProductRepository;
import kitchenpos.product.ui.ProductRequest;
import kitchenpos.product.ui.ProductResponse;
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
        final Product product = new Product(productRequest.getName(), productRequest.getPrice());
        final Product saved = productRepository.save(product);

        return ProductResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
