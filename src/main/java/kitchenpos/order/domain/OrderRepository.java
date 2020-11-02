package kitchenpos.order.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByTableIdInAndOrderStatusIn(List<Long> tableIds, List<OrderStatus> orderStatuses);

    boolean existsByTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);

    List<Order> findAllByTableIdIn(List<Long> tableIds);
}
