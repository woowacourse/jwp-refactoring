package kitchenpos.application;

import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.ui.dto.product.ProductRequest;
import kitchenpos.ui.dto.product.ProductResponse;
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
    public ProductResponse create(final ProductRequest request) {
        final Product product = new Product(request.getName(), request.getPrice());
        final Product savedProduct = productRepository.save(product);
        return ProductResponse.of(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        final List<Product> products = productRepository.findAll();
        return ProductResponse.toList(products);
    }
}
