package kitchenpos.domain.order;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
        final List<OrderStatus> orderStatuses);

    @Query(value = "select o.* from Orders o "
        + "join ORDER_TABLE ot on o.order_table_id = ot.id "
        + "where ot.table_group_id = :tableGroupId",
        nativeQuery = true)
    Set<Order> findAllByTableGroupId(@Param("tableGroupId") final Long tableGroupId);
}
