package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.dto.ProductResponse;
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
    public ProductResponse create(final ProductCreateRequest productCreateRequest) {
        Product savedProduct
            = productRepository.save(productCreateRequest.toEntity(productCreateRequest));

        return ProductResponse.of(savedProduct);
    }

    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();

        return ProductResponse.toResponseList(products);
    }
}
