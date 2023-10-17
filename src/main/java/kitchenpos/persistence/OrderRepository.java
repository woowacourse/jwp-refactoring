package kitchenpos.persistence;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);

    List<Order> findAllByOrderTableIdIn(List<Long> orderTableIds);

    @Query("SELECT o FROM orders o JOIN FETCH o.orderLineItems")
    List<Order> findAllWithFetch();
}
