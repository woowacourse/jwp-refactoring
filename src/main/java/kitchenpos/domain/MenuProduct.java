package kitchenpos.domain;

public class MenuProduct {

    private Long seq;
    private Long menuId;
    private Long productId;
    private Quantity quantity;

    public MenuProduct(Long seq, Long menuId, Long productId, Quantity quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(Long menuId, Long productId, Quantity quantity) {
        this(null, menuId, productId, quantity);
    }

    public MenuProduct(Long productId, Quantity quantity) {
        this(null, null, productId, quantity);
    }

    public static MenuProduct of(Long productId, long quantity) {
        return new MenuProduct(productId, Quantity.valueOf(quantity));
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

    public Quantity getQuantity() {
        return quantity;
    }

    public long getQuantityValue() {
        return quantity.getValue();
    }
}
