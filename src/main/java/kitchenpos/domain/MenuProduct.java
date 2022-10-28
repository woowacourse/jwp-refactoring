package kitchenpos.domain;

import java.math.BigDecimal;

public class MenuProduct {

    private Long seq;
    private Long menuId;
    private Long productId;
    private BigDecimal price;
    private long quantity;

    public MenuProduct(final Long productId, final long quantity) {
        this(null, null, productId, quantity);
    }

    public MenuProduct(final Long seq, final Long menuId, final Long productId, final long quantity) {
        this(seq, menuId, productId, null, quantity);
    }

    public MenuProduct(final Long seq, final Long menuId, final Long productId, final BigDecimal price,
                       final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    public static MenuProduct of(final Product product, final long quantity) {
        return new MenuProduct(null, null, product.getId(), product.getPrice(), quantity);
    }

    public BigDecimal amount() {
        return price.multiply(BigDecimal.valueOf(quantity));
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

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}
