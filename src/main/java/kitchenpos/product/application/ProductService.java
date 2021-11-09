package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.response.ProductResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final Product product) {
        return ProductResponse.from(productRepository.save(product));
    }

    public List<ProductResponse> list() {
        return productRepository.findAll()
            .stream()
            .map(ProductResponse::from)
            .collect(Collectors.toList());
    }

    public long countProductInIds(List<Long> productIds) {
        return productRepository.countProductInIds(productIds);
    }
}
