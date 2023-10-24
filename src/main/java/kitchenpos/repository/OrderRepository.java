package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END "
            + "FROM Orders o "
            + "WHERE o.orderTableId = (:orderTableId) AND o.orderStatus IN (:orderStatuses)")
    boolean existsByOrderTableIdAndOrderStatusIn(
            @Param("orderTableId") Long orderTableId,
            @Param("orderStatuses") List<OrderStatus> orderStatuses
    );

    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END "
            + "FROM Orders o "
            + "WHERE o.orderTableId IN (:orderTableIds) AND o.orderStatus IN (:orderStatuses)")
    boolean existsByOrderTableIdInAndOrderStatusIn(
            @Param("orderTableIds") List<Long> orderTableIds,
            @Param("orderStatuses") List<OrderStatus> orderStatuses
    );
}
