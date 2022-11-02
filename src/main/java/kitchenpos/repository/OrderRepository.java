package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.entity.Order;
import kitchenpos.domain.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END" +
    " FROM orders WHERE order_table_id = (:orderTableId) AND order_status IN (:orderStatuses)", nativeQuery = true)
    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    List<Order> findAllByIdIn(List<Long> orderTableIds);
}
