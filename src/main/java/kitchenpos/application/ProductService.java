package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final Product product) {
        Product validProduct = new Product(product.getName(), product.getPrice());
        return productRepository.save(validProduct);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
