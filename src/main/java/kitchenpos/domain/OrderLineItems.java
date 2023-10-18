package kitchenpos.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class OrderLineItems {

  private final List<OrderLineItem2> orderLineItems;

  public OrderLineItems(final List<OrderLineItem2> orderLineItems) {
    validateOrderLineItems(orderLineItems);
    this.orderLineItems = orderLineItems;
  }

  private void validateOrderLineItems(final List<OrderLineItem2> orderLineItems) {
    if (CollectionUtils.isEmpty(orderLineItems)) {
      throw new IllegalArgumentException();
    }
  }

  public List<Long> getMenuIds() {
    return orderLineItems.stream()
        .map(OrderLineItem2::getMenuId)
        .collect(Collectors.toList());
  }

  public int size() {
    return orderLineItems.size();
  }
}
