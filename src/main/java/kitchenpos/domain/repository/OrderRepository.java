package kitchenpos.domain.repository;

import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Order o WHERE o.orderTableId = :orderTableId AND o.orderStatus IN (:status)")
    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> status);
}
