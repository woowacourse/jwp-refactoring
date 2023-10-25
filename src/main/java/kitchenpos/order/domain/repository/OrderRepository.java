package kitchenpos.order.domain.repository;

import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Order o WHERE o.orderTableId = :orderTableId AND o.orderStatus IN (:status)")
    boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<String> status);

    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Order o WHERE o.orderTableId IN (:orderTableIds) AND o.orderStatus IN (:status)")
    boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds, final List<String> status);
}
