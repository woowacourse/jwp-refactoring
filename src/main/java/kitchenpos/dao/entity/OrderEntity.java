package kitchenpos.dao.entity;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderLineItem;

public class OrderEntity {

  private Long id;
  private Long orderTableId;
  private String orderStatus;
  private LocalDateTime orderedTime;

  public OrderEntity(
      final Long id,
      final Long orderTableId,
      final String orderStatus,
      final LocalDateTime orderedTime
  ) {
    this.id = id;
    this.orderTableId = orderTableId;
    this.orderStatus = orderStatus;
    this.orderedTime = orderedTime;
  }

  public OrderEntity(
      final Long orderTableId,
      final String orderStatus,
      final LocalDateTime orderedTime
  ) {
    this(null, orderTableId, orderStatus, orderedTime);
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
}
