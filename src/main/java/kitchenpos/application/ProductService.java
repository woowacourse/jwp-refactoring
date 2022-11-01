package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.JpaProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.ProductResponse;
import kitchenpos.ui.dto.request.ProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {
    private final JpaProductRepository productRepository;

    public ProductService(final JpaProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(final ProductRequest request) {
        return ProductResponse.from(productRepository.save(request.toProduct()));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        final List<Product> products = productRepository.findAll();
        final List<ProductResponse> productResponses = products.stream().map(ProductResponse::from)
                .collect(Collectors.toList());

        return productResponses;
    }
}
