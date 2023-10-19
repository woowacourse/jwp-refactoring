package kitchenpos.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    default Order getById(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("해당하는 주문이 존재하지 않습니다."));
    }

    List<Order> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    List<Order> findAllByOrderTableIn(List<OrderTable> orderTables);
}
