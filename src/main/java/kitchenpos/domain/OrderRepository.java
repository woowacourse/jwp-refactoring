package kitchenpos.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    default Order getById(Long id) {
        return findById(id).orElseThrow(() -> new OrderException("해당하는 주문이 존재하지 않습니다."));
    }

    List<Order> findAllByOrderTableIn(List<OrderTable> orderTables);

    Optional<Order> findByOrderTable(OrderTable orderTable);
}
