package kitchenpos.order.domain.repository;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatus);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableId, List<OrderStatus> orderStatus);
}
