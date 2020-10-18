package kitchenpos.domain;

public class MenuProduct {
    private final Long seq;
    private final Long productId;
    private final long quantity;
    private Long menuId;

    public MenuProduct(final Long seq, final Long productId, final long quantity, final Long menuId) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
        this.menuId = menuId;
    }

    public MenuProduct(final Long seq, final Long productId, final long quantity) {
        this(seq, productId, quantity, null);
    }

    public MenuProduct(final Long productId, final long quantity) {
        this(null, productId, quantity);
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
}
