package kitchenpos.product;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductCreateResponse;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductCreateResponse create(final ProductCreateRequest request) {
        final Product product = new Product(request.getName(), request.getPrice());
        final Product savedProduct = productRepository.save(product);

        return ProductCreateResponse.of(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        final List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::of)
                .collect(toList());
    }
}
