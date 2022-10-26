package kitchenpos.domain;

public class MenuProduct {

    private final Long seq;
    private Long menuId;
    private final Long productId;
    private final long quantity;

    public MenuProduct(final Long seq,final Long menuId,final Long productId,final long quantity) {
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
