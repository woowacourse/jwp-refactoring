package kitchenpos.domain.order.repository;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends Repository<Order, Long> {

    Order save(final Order order);

    List<Order> findAll();

    Optional<Order> findById(final Long id);

    boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
                                                   final List<OrderStatus> orderStatuses);

    boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<OrderStatus> orderStatuses);
}
