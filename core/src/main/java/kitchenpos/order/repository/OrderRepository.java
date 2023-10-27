package kitchenpos.order.repository;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.exception.OrderException.NotExistsOrderException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableInAndOrderStatusIn(final List<OrderTable> orderTables,
                                                 final List<OrderStatus> orderStatuses);

    default Order getById(final Long orderId) {
        return findById(orderId)
                .orElseThrow(() -> new NotExistsOrderException(orderId));
    }

}
