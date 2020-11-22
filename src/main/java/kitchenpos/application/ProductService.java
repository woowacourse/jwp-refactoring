package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.dto.ProductResponse;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(ProductCreateRequest productCreateRequest) {
        return ProductResponse.of(productRepository.save(productCreateRequest.toEntity()));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return ProductResponse.ofList(productRepository.findAll());
    }
}
