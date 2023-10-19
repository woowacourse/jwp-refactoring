package kitchenpos.repository;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    default Order findOrderById(final Long orderId) {
        return findById(orderId).orElseThrow(() -> new EmptyResultDataAccessException("주문 식별자값으로 주문을 조회할 수 없습니다.", 1));
    }

    @Query("select case when coalesce(count(1), 0) > 0 then true else false end " +
           " from Order o " +
           " where o.orderTable.id = :orderTableId " +
           " and o.orderStatus in :orderStatuses ")
    boolean existsByOrderTableIdAndOrderStatusIn(@Param("orderTableId") final Long orderTableId,
                                                 @Param("orderStatuses") final List<OrderStatus> orderStatuses);

    @Query("select case when coalesce(count(1), 0) > 0 then true else false end " +
           " from Order o " +
           " where o.orderTable.id in :orderTableIds " +
           " and o.orderStatus in :orderStatuses ")
    boolean existsByOrderTableIdInAndOrderStatusIn(@Param("orderTableIds") final List<Long> orderTableIds,
                                                   @Param("orderStatuses") final List<OrderStatus> orderStatuses);
}
