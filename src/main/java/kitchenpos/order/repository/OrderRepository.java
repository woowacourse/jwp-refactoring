package kitchenpos.order.repository;

import java.util.List;
import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds, final List<String> orderStatuses);

    @Query("SELECT DISTINCT o "
            + "FROM orders o "
            + "JOIN FETCH o.orderLineItems.value")
    List<Order> findAll();
}
