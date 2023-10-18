package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order2 {

  private Long id;
  private OrderTable2 orderTable;
  private OrderStatus orderStatus;
  private LocalDateTime orderedTime;
  private List<OrderLineItem2> orderLineItems;

  public Order2(
      final Long id, final OrderTable2 orderTable,
      final OrderStatus orderStatus, final LocalDateTime orderedTime,
      final List<OrderLineItem2> orderLineItems
  ) {
    this.id = id;
    this.orderTable = orderTable;
    this.orderStatus = orderStatus;
    this.orderedTime = orderedTime;
    this.orderLineItems = orderLineItems;
  }

  public Order2(
      final OrderTable2 orderTable,
      final OrderStatus orderStatus,
      final LocalDateTime orderedTime,
      final List<OrderLineItem2> orderLineItems
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

  public OrderTable2 getOrderTable() {
    return orderTable;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }

  public LocalDateTime getOrderedTime() {
    return orderedTime;
  }

  public List<OrderLineItem2> getOrderLineItems() {
    return orderLineItems;
  }
}
