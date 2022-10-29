package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderLineItem;

public interface OrderLineItemRepository {

    OrderLineItem save(OrderLineItem entity);

    Optional<OrderLineItem> findById(Long id);

    List<OrderLineItem> findAll();

    List<OrderLineItem> findAllByOrderId(Long orderId);
}
