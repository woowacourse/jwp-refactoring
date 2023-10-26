package kitchenpos.order.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableLogRepository extends JpaRepository<OrderTableLog, Long> {

    OrderTableLog findByOrder(Order order);

    List<OrderTableLog> findAllByOrderIn(List<Order> orders);

    List<OrderTableLog> findAllByOrderTableId(Long orderTableId);
}
