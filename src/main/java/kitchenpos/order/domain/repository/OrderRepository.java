package kitchenpos.order.domain.repository;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId,
                                                 final List<OrderStatus> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTables,
                                                   final List<OrderStatus> orderStatuses);
}
