package kitchenpos.product.infrastructure;

import java.util.List;
import java.util.Optional;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductDao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class JpaProductDao implements ProductDao {

    private JpaProductRepository productRepository;

    public JpaProductDao(JpaProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product entity) {
        return productRepository.save(entity);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }
}


