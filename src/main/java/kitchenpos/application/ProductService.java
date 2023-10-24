package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.domain.dto.ProductRequest;
import kitchenpos.domain.dto.ProductResponse;
import kitchenpos.domain.repository.ProductRepository;
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

        productRepository.save(product);

        return ProductResponse.from(product);
    }

    public List<ProductResponse> list() {
        final List<Product> products = productRepository.findAll();

        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
