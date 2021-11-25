package kitchenpos.product.application;

import java.util.List;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.product.ui.request.ProductRequest;
import kitchenpos.product.ui.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        Product product = productRepository.save(request.toEntity());

        return ProductResponse.from(product);
    }

    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();

        return ProductResponse.of(products);
    }
}
