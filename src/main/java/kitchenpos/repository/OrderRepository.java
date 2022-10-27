package kitchenpos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

public interface OrderRepository extends Repository<Order, Long> {
    Order save(Order order);

    Optional<Order> findById(Long orderId);

    List<Order> findAll();

    boolean existsByOrderTableAndOrderStatusIn(OrderTable orderTable, List<OrderStatus> asList);

    boolean existsByOrderTableInAndOrderStatusIn(List<OrderTable> orderTables, List<OrderStatus> asList);
}
