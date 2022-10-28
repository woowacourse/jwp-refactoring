package kitchenpos.domain;

import java.math.BigDecimal;

public class MenuProduct {

    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final long quantity;
    private final BigDecimal price;

    public MenuProduct(Long seq, Long menuId, Long productId, long quantity, BigDecimal price) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public MenuProduct(Long menuId, Long productId, long quantity, BigDecimal price) {
        this(null, menuId, productId, quantity, price);
    }

    public MenuProduct(Long productId, long quantity, BigDecimal price) {
        this(null, null, productId, quantity, price);
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

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getAmount() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
