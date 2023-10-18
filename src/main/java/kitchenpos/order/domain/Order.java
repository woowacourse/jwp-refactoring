package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {

  private Long id;
  private Long orderTableId;
  private String orderStatus;
  private LocalDateTime orderedTime;
  private List<OrderLineItem> orderLineItems;

  public Order(final Long id, final Long orderTableId, final String orderStatus,
      final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
    this.id = id;
    this.orderTableId = orderTableId;
    this.orderStatus = orderStatus;
    this.orderedTime = orderedTime;
    this.orderLineItems = orderLineItems;
  }

  public Order(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
    this.orderTableId = orderTableId;
    this.orderLineItems = orderLineItems;
  }

  public Order(final Long id, final Long orderTableId, final String orderStatus,
      final LocalDateTime orderedTime) {
    this(id, orderTableId, orderStatus, orderedTime, new ArrayList<>());
  }

  public Long getId() {
    return id;
  }

  public Long getOrderTableId() {
    return orderTableId;
  }

  public String getOrderStatus() {
    return orderStatus;
  }

  public LocalDateTime getOrderedTime() {
    return orderedTime;
  }

  public List<OrderLineItem> getOrderLineItems() {
    return orderLineItems;
  }
}
