package kitchenpos.menu.application.dto.request;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductCreateRequest {

  private Long productId;
  private long quantity;

  public MenuProductCreateRequest(final Long productId, final long quantity) {
    this.productId = productId;
    this.quantity = quantity;
  }

  public MenuProductCreateRequest() {
  }

  public Long getProductId() {
    return productId;
  }

  public long getQuantity() {
    return quantity;
  }

  public MenuProduct toMenuProduct() {
    return new MenuProduct(productId, quantity);
  }
}
