package kitchenpos.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END" +
        " FROM Order o WHERE  o.orderTable.id = (:orderTableIds) AND o.orderStatus IN (:orderStatuses)")
    boolean existsByOrderTableIdAndOrderStatusIn(
        @Param("orderTableId") Long orderTableId,
        @Param("orderStatuses") List<OrderStatus> orderStatuses
    );

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END" +
        " FROM Order o WHERE  o.orderTable.id IN (:orderTableIds) AND o.orderStatus IN (:orderStatuses)")
    boolean existsByOrderTableIdInAndOrderStatusIn(
        @Param("orderTableIds") List<Long> orderTableIds,
        @Param("orderStatuses") List<OrderStatus> orderStatuses
    );
}
