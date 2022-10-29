package kitchenpos.repository;

import java.util.Optional;
import kitchenpos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
