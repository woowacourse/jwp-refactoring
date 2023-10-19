package kitchenpos.order.application.dto;

import java.time.LocalDateTime;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

public class OrderPersistence {

  private Long id;
  private Long orderTableId;
  private String orderStatus;
  private LocalDateTime orderedTime;


  public OrderPersistence(final Long id, final Long orderTableId, final String orderStatus,
      final LocalDateTime orderedTime) {
    this.id = id;
    this.orderTableId = orderTableId;
    this.orderStatus = orderStatus;
    this.orderedTime = orderedTime;
  }

  public OrderPersistence() {
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

  public static OrderPersistence from(final Order order) {
    return new OrderPersistence(order.getId(), order.getOrderTableId(),
        order.getOrderStatus().name(), order.getOrderedTime());
  }

  public Order toOrder() {
    return new Order(id, orderTableId, OrderStatus.valueOf(orderStatus), orderedTime);
  }
}
