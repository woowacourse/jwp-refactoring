package kitchenpos.domain;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableAndOrderStatusIn(final OrderTable savedOrderTable,
        final List<OrderStatus> orderStatuses);

    Set<Order> findAllByOrderTableIn(List<OrderTable> orderTables);
}
