package kitchenpos.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    default Order getBy(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("그런 주문은 없습니다"));
    }

    List<Order> findAllByOrderTableId(Long orderTableId);
}
