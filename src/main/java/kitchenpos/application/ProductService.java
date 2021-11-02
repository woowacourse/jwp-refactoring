package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.Repository.ProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.ui.request.ProductRequest;
import kitchenpos.ui.response.ProductResponse;
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
        Product product = request.toEntity();
        product.validatePrice();

        Product save = productRepository.save(product);

        return ProductResponse.from(save);
    }

    public List<ProductResponse> list() {
        return productRepository.findAll()
            .stream()
            .map(ProductResponse::from)
            .collect(Collectors.toList());
    }
}
