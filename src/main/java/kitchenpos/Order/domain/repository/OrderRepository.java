package kitchenpos.order.domain.repository;

import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatus);

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatus);

    @EntityGraph(attributePaths = {"orderLineItems"})
    List<Order> findAll();

    @EntityGraph(attributePaths = {"orderLineItems"})
    Optional<Order> findById(Long id);
}
