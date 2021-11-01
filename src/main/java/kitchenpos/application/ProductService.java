package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
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
        Product product = new Product(productRequest.getName(),
            BigDecimal.valueOf(productRequest.getPrice()));

        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(IllegalArgumentException::new);
    }
}
