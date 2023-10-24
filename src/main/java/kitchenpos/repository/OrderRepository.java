package kitchenpos.repository;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT CASE " +
            "WHEN COUNT(o.id) > 0 " +
            "THEN TRUE ELSE FALSE " +
            "END " +
            "FROM Order o WHERE o.orderTableId = (:orderTableId) AND o.orderStatus IN (:orderStatuses)")
    boolean existsByOrderTableIdAndOrderStatusIn(
            @Param("orderTableId") Long orderTableId,
            @Param("orderStatuses") List<OrderStatus> list
    );

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> list);

}
