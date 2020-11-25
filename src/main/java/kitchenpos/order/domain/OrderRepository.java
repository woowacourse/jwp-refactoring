package kitchenpos.order.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);
}
