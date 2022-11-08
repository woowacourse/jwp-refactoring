package kitchenpos.core.menu.domain;

import java.math.BigDecimal;

public class MenuProduct {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;
    private BigDecimal price;

    private MenuProduct() {
    }

    private MenuProduct(final Long seq,
                        final Long menuId,
                        final Long productId,
                        final long quantity,
                        final BigDecimal price) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public static MenuProduct of(final Long seq,
                                 final Long menuId,
                                 final Long productId,
                                 final long quantity,
                                 final BigDecimal price) {
        return new MenuProduct(seq, menuId, productId, quantity, price);
    }

    public static MenuProduct of(final Long menuId,
                       final Long productId,
                       final long quantity,
                       final BigDecimal price) {
        return of(null, menuId, productId, quantity, price);
    }

    public static MenuProduct createForEntity(final Long seq,
                                              final Long menuId,
                                              final Long productId,
                                              final long quantity) {
        return new MenuProduct(seq, menuId, productId, quantity, null);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void changeMenu(final Long menuId) {
        this.menuId = menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
