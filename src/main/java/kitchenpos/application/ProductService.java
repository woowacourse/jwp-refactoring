package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.request.ProductCreateRequest;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;
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
    public ProductResponse create(final ProductCreateRequest request) {
        final Product product = new Product(request.getName(), Price.from(request.getPrice()));
        return ProductResponse.from(productRepository.save(product));
    }

    public List<ProductResponse> list() {
        return ProductResponse.from(productRepository.findAll());
    }
}
