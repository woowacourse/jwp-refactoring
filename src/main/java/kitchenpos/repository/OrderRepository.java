package kitchenpos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value="select o from Order o where o.orderTable.id in :orderTableIds and o.orderStatus in :status")
    List<Order> findByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> status);
}
