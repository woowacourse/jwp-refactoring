package kitchenpos.order_table.domain.repository;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order_table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    default OrderTable getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id가 " + id + "인 OrderTable을 찾을 수 없습니다."));
    }

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END FROM Order o WHERE o.orderTableId = :orderTableId AND o.orderStatus IN :orderStatuses")
    boolean existsByOrderTableIdAndOrderStatusIn(@Param("orderTableId") Long orderTableId, @Param("orderStatuses") List<OrderStatus> orderStatuses);

    @Query("SELECT ot FROM OrderTable ot WHERE ot.id IN :orderTableIds")
    List<OrderTable> findAllByIdIn(@Param("orderTableIds") List<Long> orderTableIds);

}
