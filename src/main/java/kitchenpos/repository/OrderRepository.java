package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.entity.Order;
import kitchenpos.domain.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<OrderStatus> orderStatus);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END " +
            "FROM Order o " +
            "WHERE o.orderTable.id IN :orderTableIds AND o.orderStatus IN :orderStatus"
    )
    boolean existsByOrderTableIdInAndOrderStatusIn(@Param("orderTableIds") List<Long> orderTableIds,
                                                   @Param("orderStatus") List<OrderStatus> orderStatus);
}
