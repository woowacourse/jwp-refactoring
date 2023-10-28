package kitchenpos.order.domain;

public class OrderLineItem {

  private Long seq;
  private Long menuId;
  private long quantity;

  public OrderLineItem(
      final Long seq,
      final Long menuId,
      final long quantity
  ) {
    this.seq = seq;
    this.menuId = menuId;
    this.quantity = quantity;
  }

  public OrderLineItem(final Long menuId, final long quantity) {
    this(null, menuId, quantity);
  }

  public Long getSeq() {
    return seq;
  }

  public Long getMenuId() {
    return menuId;
  }

  public long getQuantity() {
    return quantity;
  }
}
