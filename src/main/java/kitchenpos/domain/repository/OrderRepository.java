package kitchenpos.domain.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByOrderTableIdIn(List<Long> orderTableIds);

    Optional<Order> findByOrderTableId(Long orderTableId);
}
