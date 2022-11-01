package kitchenpos.domain;

import java.math.BigDecimal;

public class MenuProduct {
    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    private final BigDecimal price;

    private MenuProduct(Long seq, Long menuId, Long productId, long quantity, BigDecimal price) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public MenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        this(seq, menuId, productId, quantity, null);
    }

    public MenuProduct(Long menuId, Long productId, long quantity) {
        this(null, menuId, productId, quantity, null);
    }

    public MenuProduct(Long productId, BigDecimal price, long quantity) {
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
        BigDecimal multiply = price.multiply(BigDecimal.valueOf(quantity));
        return multiply;
    }
}
