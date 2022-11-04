package kitchenpos.order.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

public interface OrderRepository extends Repository<Order, Long> {

    Order save(final Order order);

    List<Order> findAll();

    Optional<Order> findById(final Long id);

    boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
                                                   final List<OrderStatus> orderStatuses);

    boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<OrderStatus> orderStatuses);
}
