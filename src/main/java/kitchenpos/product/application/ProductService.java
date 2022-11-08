package kitchenpos.product.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.product.application.request.ProductRequest;
import kitchenpos.product.application.response.ProductResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(final ProductRequest request) {
        final Product product = Product.of(request.getName(), request.getPrice());
        final Product savedProduct = productRepository.save(product);

        return new ProductResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        final List<Product> products = productRepository.findAll();

        return products.stream()
                .map(ProductResponse::new)
                .collect(toList());
    }
}
