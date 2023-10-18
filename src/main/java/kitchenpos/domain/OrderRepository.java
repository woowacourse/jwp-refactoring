package kitchenpos.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o "
            + "FROM orders o "
            + "WHERE o.orderTable.id = :orderTableId")
    List<Order> findByOrderByOrderTableId(@Param("orderTableId") final Long orderTableId);

    @Query("SELECT o "
            + "FROM orders o "
            + "WHERE o.orderTable.tableGroup.id = :tableGroupId")
    List<Order> findByOrderByTableGroupId(@Param("tableGroupId") final Long tableGroupId);
}
