package kitchenpos.domain.repository;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
//    SELECT e.name, CASE WHEN (e.salary >= 100000) THEN 1 WHEN (e.salary < 100000) THEN 2 ELSE 0 END FROM Employee e
    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END FROM Order o WHERE o.orderTable.id = (:orderTableId) AND o.orderStatus IN (:orderStatuses)")
    boolean existsByOrderTableIdAndOrderStatusIn(@Param("orderTableId") Long orderTableId, @Param("orderStatuses") List<OrderStatus> orderStatuses);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END FROM Order o WHERE o.orderTable.id IN (:orderTableIds) AND o.orderStatus IN (:orderStatuses)")
    boolean existsByOrderTableIdInAndOrderStatusIn(@Param("orderTableIds") List<Long> orderTableIds, @Param("orderStatuses") List<OrderStatus> orderStatuses);
}
