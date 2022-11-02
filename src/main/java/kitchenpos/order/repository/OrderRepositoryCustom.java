package kitchenpos.order.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

public interface OrderRepositoryCustom {

    boolean existsByIdAndStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);

    boolean existsByIdInAndStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses);

    Optional<Order> findWithOrderItemsById(Long id);
 }
