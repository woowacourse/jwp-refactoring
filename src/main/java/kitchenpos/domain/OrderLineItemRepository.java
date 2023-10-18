package kitchenpos.domain;

import java.util.List;
import java.util.Optional;

public interface OrderLineItemRepository {

  OrderLineItem2 save(OrderLineItem2 orderLineItem, Order2 order);

  Optional<OrderLineItem2> findById(Long id);

  List<OrderLineItem2> findAll();

  List<OrderLineItem2> findAllByOrderId(Long orderId);
}
