package kitchenpos.domain;

public class MenuProduct {
    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    public MenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(Long menuId, MenuProduct menuProduct) {
        this.seq = null;
        this.menuId = menuId;
        this.productId = menuProduct.productId;
        this.quantity = menuProduct.quantity;
    }

    public MenuProduct(Long productId, long quantity) {
        this(null, null, productId, quantity);
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
