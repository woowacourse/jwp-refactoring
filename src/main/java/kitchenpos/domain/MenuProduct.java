package kitchenpos.domain;

import java.util.Objects;

public class MenuProduct {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Long seq, final Long menuId, final Long productId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(final Long menuId, final Long productId, final long quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuProduct)) return false;
        MenuProduct that = (MenuProduct) o;
        return quantity == that.quantity && Objects.equals(seq, that.seq) && Objects.equals(menuId, that.menuId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, menuId, productId, quantity);
    }
}
