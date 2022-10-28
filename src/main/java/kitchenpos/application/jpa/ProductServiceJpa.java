package kitchenpos.application.jpa;

import java.util.List;
import kitchenpos.domain.entity.Product;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceJpa {

    private final ProductRepository productRepository;

    public ProductServiceJpa(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void create(Product product) {
        productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
