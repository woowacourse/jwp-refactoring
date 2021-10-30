package kitchenpos.application;

import kitchenpos.repository.ProductRepository;
import kitchenpos.domain.Product;
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
    public Product create(final Product product) {
        final Product newProduct = new Product.Builder()
                .name(product.getName())
                .price(product.getPrice())
                .build();

        return productRepository.save(newProduct);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
