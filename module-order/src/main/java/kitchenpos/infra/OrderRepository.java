package kitchenpos.infra;

import java.util.NoSuchElementException;
import java.util.Optional;
import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    default Order getById(Long id) {
        return findById(id).orElseThrow(() -> new NoSuchElementException("주문이 존재하지 않습니다."));
    }

    default Order getByOrderTableId(Long orderTableId) {
        return findByOrderTableId(orderTableId).orElseThrow(() -> new NoSuchElementException("주문이 존재하지 않습니다."));
    }

    Optional<Order> findByOrderTableId(Long orderTableId);
}
