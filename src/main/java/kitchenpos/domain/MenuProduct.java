package kitchenpos.domain;

public class MenuProduct {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProduct() {
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

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public void setProductId(final Long productId) {
        this.productId = productId;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}
