package kitchenpos.order.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long>{
    @Query("select o from Order o join fetch o.orderLineItems ol")
    List<Order> findAll();

    @Query("select o from Order o where o.orderTableId = ?1")
    Optional<Order> findByOrderTableId(Long orderTableId);

    @Query("select o from Order o where o.orderTableId in ?1")
    List<Order> findAllByOrderTableId(List<Long> orderTableIds);
}
