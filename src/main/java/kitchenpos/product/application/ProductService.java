package kitchenpos.product.application;

import java.util.List;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        Product product = Product.builder()
            .name(request.getName())
            .price(request.getPrice())
            .build();

        Product savedProduct = productRepository.save(product);
        return ProductResponse.from(savedProduct);
    }

    @Transactional
    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();
        return ProductResponse.listFrom(products);
    }
}
