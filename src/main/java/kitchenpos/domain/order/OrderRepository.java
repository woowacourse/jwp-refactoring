package kitchenpos.domain.order;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
        final List<OrderStatus> orderStatuses);

    Set<Order> findAllByOrderTableIdIn(List<Long> orderTableIds);
}
