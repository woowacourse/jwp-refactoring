package kitchenpos.product.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    int countByIdIn(List<Long> productIds);
}
