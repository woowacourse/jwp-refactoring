package kitchenpos.order.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

public interface OrderLineItemDao {

  OrderLineItemEntity save(OrderLineItemEntity entity);

  Optional<OrderLineItemEntity> findById(Long id);

  List<OrderLineItemEntity> findAll();

  List<OrderLineItemEntity> findAllByOrderId(Long orderId);
}
