package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.exception.ProductNotFoundException;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.request.ProductRequest;
import kitchenpos.ui.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public Product findById(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(
                String.format("%d ID를 가진 Product가 존재하지 않습니다.", productId))
            );
    }
}
