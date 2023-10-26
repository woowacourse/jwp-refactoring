package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select distinct o from Order o join fetch o.orderLineItems.orderLineItems")
    List<Order> findAllWithOrderLineItems();

    List<Order> findByOrderTableId(Long orderTableId);
}
