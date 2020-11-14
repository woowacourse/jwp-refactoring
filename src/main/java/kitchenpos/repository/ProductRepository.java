package kitchenpos.repository;

import kitchenpos.domain.menu.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    int countByIdIn(List<Long> ids);
}
