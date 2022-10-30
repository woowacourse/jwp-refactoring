package kitchenpos.domain;

import java.math.BigDecimal;

public class MenuProduct {

    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final BigDecimal price;
    private final long quantity;

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

    public BigDecimal calculateTotalAmount() {
        return price.multiply(BigDecimal.valueOf(quantity));
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

    public BigDecimal getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }
}
