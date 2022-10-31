package kitchenpos.dao.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import kitchenpos.dao.ProductDao;
import kitchenpos.dao.jpa.repository.JpaProductRepository;
import kitchenpos.domain.Product;

@Primary
@Repository
public class JpaProductDao implements ProductDao {

    private final JpaProductRepository productRepository;

    public JpaProductDao(final JpaProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(final Product entity) {
        return productRepository.save(entity);
    }

    @Override
    public Optional<Product> findById(final Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product getById(final Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("product not found"));
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }
}
