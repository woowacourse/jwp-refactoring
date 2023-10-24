package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.product.ProductRequest;
import kitchenpos.application.dto.product.ProductResponse;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;
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
        final Product product = new Product(productRequest.getName(), Price.from(productRequest.getPrice()));
        productRepository.save(product);

        return ProductResponse.from(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        final List<Product> products = productRepository.findAll();

        return products.stream()
            .map(ProductResponse::from)
            .collect(Collectors.toList());
    }
}
