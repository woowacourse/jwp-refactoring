package kitchenpos.order.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds, final List<OrderStatus> orderStatuses);
    boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<OrderStatus> orderStatuses);
}
