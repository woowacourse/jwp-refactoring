package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product savedProduct = productRepository.save(productRequest.toEntity());
        return ProductResponse.from(savedProduct);
    }

    public List<ProductResponse> list() {
        return ProductResponse.listFrom(productRepository.findAll());
    }
}
