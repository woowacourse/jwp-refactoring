package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.dto.ProductResponse;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(final ProductCreateRequest request) {
        final Product product = new Product(request.getName(), request.getPrice());
        productRepository.save(product);
        return ProductResponse.toResponse(product);
    }

    public List<ProductResponse> list() {
        final List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::toResponse)
                .collect(Collectors.toList());
    }
}
