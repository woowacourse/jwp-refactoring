package kitchenpos.order.application.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;

public class OrderCreateRequest {

  private Long orderTableId;

  private List<OrderLineItemCreateRequest> orderLineItems;

  public OrderCreateRequest(final Long orderTableId,
      final List<OrderLineItemCreateRequest> orderLineItems) {
    this.orderTableId = orderTableId;
    this.orderLineItems = orderLineItems;
  }

  public OrderCreateRequest() {
  }

  public Long getOrderTableId() {
    return orderTableId;
  }

  public List<OrderLineItemCreateRequest> getOrderLineItems() {
    return orderLineItems;
  }

  public Order toOrder() {
    return new Order(
        orderTableId,
        orderLineItems.stream()
            .map(OrderLineItemCreateRequest::toOrderLineItem)
            .collect(Collectors.toList())
    );
  }
}
