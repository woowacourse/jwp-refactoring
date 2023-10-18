package kitchenpos.domain;

public class MenuProduct {

    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final Long quantity;

    private MenuProduct(final Long seq,
                        final Long menuId,
                        final Long productId,
                        final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProduct of(final Long seq,
                                 final Long menuId,
                                 final Long productId,
                                 final long quantity) {
        return new MenuProduct(seq, menuId, productId, quantity);
    }

    public static MenuProduct of(final Long menuId,
                                 final Long productId,
                                 final long quantity) {
        return new MenuProduct(null, menuId, productId, quantity);
    }

    public static MenuProduct of(final Long productId,
                                 final long quantity) {
        return new MenuProduct(null, null, productId, quantity);
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
