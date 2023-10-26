package kitchenpos.order.domain.repository;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderTableId(final Long orderTableId);

    boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
                                                   final List<OrderStatus> orderStatuses);
}
