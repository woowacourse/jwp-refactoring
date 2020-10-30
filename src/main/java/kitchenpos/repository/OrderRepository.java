package kitchenpos.repository;

import kitchenpos.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);
}
