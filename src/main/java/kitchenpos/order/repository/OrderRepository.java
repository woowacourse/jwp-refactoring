package kitchenpos.order.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order save(Order entity);

    Optional<Order> findById(Long id);

    @Query("select distinct o"
            + " from Order o"
            + " left join fetch o.orderLineItems")
    List<Order> findAll();

    Optional<Order> findByOrderTableId(Long orderTableId);

    List<Order> findByOrderTableIdIn(List<Long> orderTableIds);
}
