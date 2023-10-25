package kitchenpos.domain.order.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o From Order o join fetch o.orderLineItems.values where o.id = :id")
    Optional<Order> findById(@Param("id") final Long id);

    List<Order> findAllByOrderTableId(final Long orderTableId);
}
