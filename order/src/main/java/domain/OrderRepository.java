package domain;

import exception.OrderException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    default Order getById(Long id) {
        return findById(id).orElseThrow(() -> new OrderException("해당하는 주문이 존재하지 않습니다."));
    }
}
