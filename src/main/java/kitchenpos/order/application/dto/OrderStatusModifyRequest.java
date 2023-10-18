package kitchenpos.order.application.dto;

import kitchenpos.order.domain.OrderStatus;

public class OrderStatusModifyRequest {

  private OrderStatus orderStatus;

  public OrderStatusModifyRequest() {
  }

  public OrderStatusModifyRequest(final OrderStatus orderStatus) {
    this.orderStatus = orderStatus;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }
}
