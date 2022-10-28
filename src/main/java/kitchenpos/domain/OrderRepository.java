package kitchenpos.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = "orderLineItems")
    Optional<Order> findByIdOrderByOrderLineItems(Long orderId);


    @EntityGraph(attributePaths = "orderLineItems")
    List<Order> findAll();
}
