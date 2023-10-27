package kitchenpos.order.domain.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Override
    @EntityGraph(attributePaths = "orderLineItems")
    Optional<Order> findById(final Long id);

    List<Order> findAllByOrderTableId(final Long orderTableId);
}
