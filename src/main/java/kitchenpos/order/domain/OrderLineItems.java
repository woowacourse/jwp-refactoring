package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class OrderLineItems {

  private final List<OrderLineItem> orderLineItems;

  public OrderLineItems(final List<OrderLineItem> orderLineItems) {
    this.orderLineItems = orderLineItems;
  }

  public OrderLineItems() {
    this(new ArrayList<>());
  }

  public List<OrderLineItem> getOrderLineItems() {
    return orderLineItems;
  }

  public List<Long> extractMenuIds() {
    return orderLineItems.stream()
        .map(OrderLineItem::getMenuId)
        .collect(Collectors.toList());
  }

  public boolean isEmpty() {
    return CollectionUtils.isEmpty(orderLineItems);
  }

  public boolean isDifferentSize(final long size) {
    return orderLineItems.size() != size;
  }
}
