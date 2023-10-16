package kitchenpos.application;

import kitchenpos.domain.product.Product;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.request.ProductRequest;
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
    public Product create(final ProductRequest productRequest) {
        final Product product = Product.from(productRequest.getName(), productRequest.getPrice());
        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
