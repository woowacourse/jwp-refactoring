package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.application.request.ProductRequest;
import kitchenpos.application.response.ProductResponse;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        final Product product = Product.of(request.getName(), request.getPrice());
        final Product savedProduct = productRepository.save(product);

        return new ProductResponse(savedProduct);
    }

    public List<ProductResponse> list() {
        final List<Product> products = productRepository.findAll();

        return products.stream()
                .map(ProductResponse::new)
                .collect(toList());
    }
}
