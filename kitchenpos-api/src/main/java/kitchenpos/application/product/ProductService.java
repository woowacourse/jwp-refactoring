package kitchenpos.application.product;

import java.util.List;
import kitchenpos.application.product.dto.ProductCreateRequest;
import kitchenpos.application.product.dto.ProductResponse;
import kitchenpos.product.Product;
import kitchenpos.product.repository.ProductRepository;
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
        final Product savedProduct = productRepository.save(createProductDataByRequest(request));
        return ProductResponse.from(savedProduct);
    }

    private Product createProductDataByRequest(final ProductCreateRequest request) {
        return new Product(request.getName(), request.getPrice());
    }

    public List<ProductResponse> list() {
        return ProductResponse.listOf(productRepository.findAll());
    }
}
