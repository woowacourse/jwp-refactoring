package kitchenpos.domain.repository;

import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    default OrderTable getById(final Long id) {
        return findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM orders WHERE order_table_id = (:orderTableId) AND order_status IN (:orderStatuses)", nativeQuery = true)
    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);
}
