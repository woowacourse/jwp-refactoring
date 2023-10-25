package kitchenpos.domain;

public class MenuProduct {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProduct(Long productId, long quantity) {
        this(null, null, productId, quantity);
    }

    public MenuProduct(Long seq, Long menuId, Long productId, long quantity) {
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
