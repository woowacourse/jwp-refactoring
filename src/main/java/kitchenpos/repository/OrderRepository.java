package kitchenpos.repository;

import kitchenpos.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select distinct o from Order o join fetch o.orderLineItems")
    List<Order> findAllWithOrderLineItems();
}
