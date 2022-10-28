package kitchenpos.domain;

import java.util.Objects;

@Deprecated
public class MenuProduct {
    private Long id;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProduct(Long id, Long menuId, Long productId, long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(Long productId, long quantity) {
        this(null, null, productId, quantity);
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuProduct that = (MenuProduct) o;
        return quantity == that.quantity && Objects.equals(menuId, that.menuId) && Objects.equals(
                productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, productId, quantity);
    }
}
