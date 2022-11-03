package kitchenpos.order.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses);

    @Query("select o "
            + " from Order o "
            + " join fetch o.orderLineItems ol ")
    List<Order> findAllWithOrderLineItems();
}
