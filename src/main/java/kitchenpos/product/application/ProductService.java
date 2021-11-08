package kitchenpos.product.application;

import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.ui.request.ProductRequest;
import kitchenpos.product.ui.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        final Product product = new Product.Builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        return ProductResponse.of(product);
    }

    public List<ProductResponse> list() {
        final List<Product> products = productRepository.findAll();
        return ProductResponse.toList(products);
    }
}
