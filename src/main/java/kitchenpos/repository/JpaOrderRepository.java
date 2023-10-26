package kitchenpos.repository;

import static kitchenpos.exception.OrderExceptionType.NOT_EXIST_ORDER_EXCEPTION;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.OrderException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderRepository extends JpaRepository<Order, Long> {

    default Order getById(Long id) {
        return findById(id).orElseThrow(() -> new OrderException(NOT_EXIST_ORDER_EXCEPTION));
    }

    default Order getByOrderTable(OrderTable orderTable) {
        return findByOrderTable(orderTable).orElseThrow(() -> new OrderException(NOT_EXIST_ORDER_EXCEPTION));
    }

    Optional<Order> findByOrderTable(OrderTable orderTable);

    List<Order> getAllByOrderTableIn(List<OrderTable> orderTables);
}
