package kitchenpos.product.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    default Product getById(Long id) {
        return findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    Product save(Product entity);

    Optional<Product> findById(Long id);

    List<Product> findAll();
}
