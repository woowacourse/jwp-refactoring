package kitchenpos.order.persistence;

import java.util.List;
import kitchenpos.order.Order;
import kitchenpos.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);

    List<Order> findAllByOrderTableIdIn(List<Long> orderTableIds);

    @Query("SELECT o FROM orders o JOIN FETCH o.orderLineItems.values")
    List<Order> findAllWithFetch();
}
