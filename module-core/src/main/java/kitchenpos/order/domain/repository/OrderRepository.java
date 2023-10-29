package kitchenpos.order.domain.repository;

import java.util.List;

import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderTableIdIn(final List<Long> orderTableIds);

    default Order getById(final Long orderId) {
        return findById(orderId).orElseThrow();
    }

}
