package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.exception.OrderException.NotExistsOrderException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableInAndOrderStatusIn(final List<OrderTable> orderTables,
                                                 final List<OrderStatus> orderStatuses);

    boolean existsByOrderTableAndOrderStatusIn(final OrderTable orderTableId, final List<OrderStatus> orderStatuses);

    default Order getById(final Long orderId) {
        return findById(orderId)
                .orElseThrow(() -> new NotExistsOrderException(orderId));
    }

}
