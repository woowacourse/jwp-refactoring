package kitchenpos.order.domain.repository;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findAllByOrderTableId(Long orderTableId);
    
    boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId,
                                                 final List<OrderStatus> orderStatuses);
}
