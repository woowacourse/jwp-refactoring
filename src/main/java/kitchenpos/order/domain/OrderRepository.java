package kitchenpos.order.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findByOrderTableId(Long orderTableId);

    List<Order> findAll();
}
