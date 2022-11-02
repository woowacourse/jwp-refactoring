package kitchenpos.order.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderTableIdIn(List<Long> orderTableIds);

    Optional<Order> findByOrderTableId(Long orderTableId);
}
