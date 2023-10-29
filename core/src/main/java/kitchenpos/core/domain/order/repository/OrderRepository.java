package kitchenpos.core.domain.order.repository;

import java.util.List;
import kitchenpos.core.domain.order.Order;
import kitchenpos.core.domain.vo.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<OrderStatus> orderStatuses);
}
