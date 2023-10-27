package kitchenpos.domain.repository;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> list);

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> list);
}
