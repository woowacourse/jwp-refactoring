package kitchenpos.infrastructure.persistence;

import kitchenpos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaProductRepository extends JpaRepository<Product, Long> {

    Product save(final Product product);

    Optional<Product> findById(final Long id);

    List<Product> findAll();
}
