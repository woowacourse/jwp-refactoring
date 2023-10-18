package kitchenpos.domain.repository;

import kitchenpos.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM orders WHERE order_table_id IN :orderTableIds AND order_status IN :orderStatuses", nativeQuery = true)
    boolean existsByOrderTableIdInAndOrderStatusIn(@Param("orderTableIds") List<Long> orderTableIds, @Param("orderStatuses") List<String> orderStatuses);

}
