package kitchenpos.menu;

import java.util.Objects;

public class MenuProduct {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProduct(final Long seq, final Long menuId, final Long productId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(final Long productId, final long quantity) {
        this(null, null, productId, quantity);
    }

    public void belongToMenu(final Long menuId) {
        this.menuId = menuId;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MenuProduct menuProduct = (MenuProduct) o;
        if (Objects.isNull(this.seq) || Objects.isNull(menuProduct.seq)) {
            return false;
        }
        return Objects.equals(seq, menuProduct.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
