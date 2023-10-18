package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Long countByIdIn(List<Long> ids);
}
