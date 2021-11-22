package kitchenpos.domain.repository;

import java.util.List;
import kitchenpos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIdIn(List<Long> ids);
}
