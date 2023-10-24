package kitchenpos.domain.order.repository;

import java.util.List;
import kitchenpos.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderTableId(final Long orderTableId);
}
