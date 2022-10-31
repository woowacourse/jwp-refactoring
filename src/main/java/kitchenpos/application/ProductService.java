package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.ProductCreateRequest;
import kitchenpos.ui.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductCreateRequest request) {
        return ProductResponse.from(productRepository.save(request.toProduct()));
    }

    public List<ProductResponse> list() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }
}
