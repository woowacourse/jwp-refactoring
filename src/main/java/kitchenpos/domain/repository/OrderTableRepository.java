package kitchenpos.domain.repository;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    default OrderTable getById(final Long id) {
        return findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END FROM Order o WHERE o.orderTable.id = :orderTableId AND o.orderStatus IN :orderStatuses")
    boolean existsByOrderTableIdAndOrderStatusIn(@Param("orderTableId") Long orderTableId, @Param("orderStatuses") List<OrderStatus> orderStatuses);

    @Query("SELECT ot FROM OrderTable ot WHERE ot.id IN :orderTableIds")
    List<OrderTable> findAllByIdIn(@Param("orderTableIds") List<Long> orderTableIds);

}
