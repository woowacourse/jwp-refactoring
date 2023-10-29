package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdAndOrderStatusIsNot(Long orderTableId, OrderStatus orderStatus);

    boolean existsByOrderTableIdInAndOrderStatusIsNot(List<Long> orderTableIds, OrderStatus orderStatuses);
}
