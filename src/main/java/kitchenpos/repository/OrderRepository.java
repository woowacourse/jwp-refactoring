package kitchenpos.repository;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses);
}
