package kitchenpos.order.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long>{
    @Query("select o from Order o join fetch o.orderLineItems ol")
    List<Order> findAll();
}
