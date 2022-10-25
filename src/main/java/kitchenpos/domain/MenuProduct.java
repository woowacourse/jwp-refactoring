package kitchenpos.domain;

public class MenuProduct {

    private Long seq;
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    public MenuProduct(Long menuId, Long productId, long quantity) {
        this(null, menuId, productId, quantity);
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

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
