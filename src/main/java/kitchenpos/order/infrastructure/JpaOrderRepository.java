package kitchenpos.order.infrastructure;

import java.util.List;
import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);
}
