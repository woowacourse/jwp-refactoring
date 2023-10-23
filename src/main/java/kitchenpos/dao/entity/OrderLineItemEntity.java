package kitchenpos.dao.entity;

public class OrderLineItemEntity {

  private Long seq;
  private Long orderId;
  private Long menuId;
  private long quantity;

  public OrderLineItemEntity(
      final Long seq,
      final Long orderId,
      final Long menuId,
      final long quantity
  ) {
    this.seq = seq;
    this.orderId = orderId;
    this.menuId = menuId;
    this.quantity = quantity;
  }

  public OrderLineItemEntity(final Long orderId, final Long menuId, final long quantity) {
    this(null, orderId, menuId, quantity);
  }

  public Long getSeq() {
    return seq;
  }

  public Long getOrderId() {
    return orderId;
  }

  public Long getMenuId() {
    return menuId;
  }

  public long getQuantity() {
    return quantity;
  }
}
