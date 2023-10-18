package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order2 {

  private Long id;
  private OrderTable2 orderTable;
  private String orderStatus;
  private LocalDateTime orderedTime;
  private List<OrderLineItem> orderLineItems;

  public Order2(
      final Long id, final OrderTable2 orderTable,
      final String orderStatus, final LocalDateTime orderedTime,
      final List<OrderLineItem> orderLineItems
  ) {
    this.id = id;
    this.orderTable = orderTable;
    this.orderStatus = orderStatus;
    this.orderedTime = orderedTime;
    this.orderLineItems = orderLineItems;
  }

  public Order2(
      final OrderTable2 orderTable,
      final String orderStatus,
      final LocalDateTime orderedTime
  ) {
    this(null, orderTable, orderStatus, orderedTime, null);
  }

  public Long getId() {
    return id;
  }

  public OrderTable2 getOrderTable() {
    return orderTable;
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
