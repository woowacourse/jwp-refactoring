package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.menu.ProductRepository;
import kitchenpos.domain.menu.Product;
import kitchenpos.dto.request.ProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(), productRequest.getPrice());
        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
