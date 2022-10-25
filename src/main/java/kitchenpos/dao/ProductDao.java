package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<Product, Long> {
    default Product getById(Long id) {
        return findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    Product save(Product entity);

    Optional<Product> findById(Long id);

    List<Product> findAll();
}
