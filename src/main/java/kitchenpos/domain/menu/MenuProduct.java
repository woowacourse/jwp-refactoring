package kitchenpos.domain.menu;

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

    public MenuProduct(final Long menuId, final Long productId, final long quantity) {
        this(null, menuId, productId, quantity);
    }

    public MenuProduct(final Long productId, final long quantity) {
        this(null, productId, quantity);
    }

    public MenuProduct(final Long productId) {
        this(productId, 1);
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
}
