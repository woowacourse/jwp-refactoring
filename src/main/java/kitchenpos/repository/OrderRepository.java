package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface OrderRepository extends Repository<Order, Long> {

    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses);

    @Query("SELECT o FROM Order o JOIN FETCH o.orderLineItems")
    List<Order> findAllWithOrderLineItems();
}
