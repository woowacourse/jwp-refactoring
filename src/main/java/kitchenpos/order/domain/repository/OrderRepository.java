package kitchenpos.order.domain.repository;

import java.util.List;
import kitchenpos.order.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderLineItems")
    List<Order> findAllWithFetch();

    List<Order> findAllByOrderTableId(Long orderTableId);

    List<Order> findAllByOrderTableIdIn(List<Long> orderTableIds);

    default Order findByIdOrThrow(Long id) {
        return findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
    }
}
