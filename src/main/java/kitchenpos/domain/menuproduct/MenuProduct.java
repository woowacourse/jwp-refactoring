package kitchenpos.domain.menuproduct;

import kitchenpos.domain.Quantity;

import java.util.Objects;

public class MenuProduct {
    private Long seq;
    private Long menuId;
    private Long productId;
    private Quantity quantity;

    public MenuProduct(final Long menuId,
                       final Long productId,
                       final Quantity quantity) {
        this(null, menuId, productId, quantity);
    }

    public MenuProduct(final Long seq,
                       final Long menuId,
                       final Long productId,
                       final Quantity quantity) {
        this.seq = seq;
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

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity.getValue();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MenuProduct that = (MenuProduct) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
