package kitchenpos.domain.order.repository;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.vo.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<OrderStatus> orderStatuses);
}
