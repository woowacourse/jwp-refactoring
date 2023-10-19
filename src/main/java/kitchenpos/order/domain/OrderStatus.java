package kitchenpos.order.domain;

public enum OrderStatus {
  COOKING, MEAL, COMPLETION;

  public boolean isEqual(final OrderStatus orderStatus) {
    return this.equals(orderStatus);
  }
}
