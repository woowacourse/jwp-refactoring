package kitchenpos.domain;

import java.math.BigDecimal;
import kitchenpos.vo.Price;

public class MenuProduct {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;
    private Price price;

    public MenuProduct(final Long productId, final long quantity, final Price price) {
        this(null, null, productId, quantity, price);
    }

    public MenuProduct(final Long seq, final Long menuId, final Long productId, final long quantity) {
        this(seq, menuId, productId, quantity, null);
    }

    public MenuProduct(final Long seq, final Long menuId, final Long productId, final long quantity,
                       final Price price) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public BigDecimal calculateAmount() {
        return price.multiply(quantity);
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
}
