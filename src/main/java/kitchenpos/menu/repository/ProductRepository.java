package kitchenpos.menu.repository;

import java.util.List;
import kitchenpos.menu.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByIdIn(List<Long> ids);

    List<Product> findAllByIdIn(List<Long> ids);
}
