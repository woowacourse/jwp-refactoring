package kitchenpos.menu.application;

import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        Product product = productRepository.save(request.toEntity());
        return ProductResponse.of(product);
    }

    public List<ProductResponse> list() {
        return ProductResponse.listOf(productRepository.findAll());
    }
}
