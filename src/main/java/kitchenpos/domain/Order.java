package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

  private Long id;
  private OrderTable orderTable;
  private OrderStatus orderStatus;
  private LocalDateTime orderedTime;
  private List<OrderLineItem> orderLineItems;

  public Order(
      final Long id, final OrderTable orderTable,
      final OrderStatus orderStatus, final LocalDateTime orderedTime,
      final List<OrderLineItem> orderLineItems
  ) {
    this.id = id;
    this.orderTable = orderTable;
    this.orderStatus = orderStatus;
    this.orderedTime = orderedTime;
    this.orderLineItems = orderLineItems;
  }

  public Order(
      final OrderTable orderTable,
      final OrderStatus orderStatus,
      final LocalDateTime orderedTime,
      final List<OrderLineItem> orderLineItems
  ) {
    this(null, orderTable, orderStatus, orderedTime, orderLineItems);
  }

  public boolean isCompletion() {
    return orderStatus == OrderStatus.COMPLETION;
  }

  public void changeStatus(final OrderStatus orderStatus) {
    this.orderStatus = orderStatus;
  }

  public Long getId() {
    return id;
  }

  public OrderTable getOrderTable() {
    return orderTable;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }

  public LocalDateTime getOrderedTime() {
    return orderedTime;
  }

  public List<OrderLineItem> getOrderLineItems() {
    return orderLineItems;
  }
}
