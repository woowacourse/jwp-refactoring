package kitchenpos.repository.order;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;

public interface OrderRepositoryCustom {

    boolean existsByIdAndStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);

    boolean existsByIdInAndStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses);

    Optional<Order> findWithOrderItemsById(Long id);
 }
