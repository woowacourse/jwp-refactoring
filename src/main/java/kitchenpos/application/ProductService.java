package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.ProductResponse;
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
    public ProductResponse create(final ProductCreateRequest request) {
        final Product product = productRepository.save(request.toProduct());
        return ProductResponse.from(product);
    }

    public List<ProductResponse> list() {
        final List<Product> products = productRepository.findAll();
        return ProductResponse.from(products);
    }
}
