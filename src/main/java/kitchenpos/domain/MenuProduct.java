package kitchenpos.domain;

import java.math.BigDecimal;
import kitchenpos.vo.Price;

public class MenuProduct {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;
    private Price productPrice;

    private MenuProduct() {
    }

    public MenuProduct(final Long productId, final long quantity, final Price productPrice) {
        this(null, null, productId, quantity, productPrice);
    }

    public MenuProduct(final Long seq, final Long menuId, final Long productId, final long quantity) {
        this(seq, menuId, productId, quantity, null);
    }

    public MenuProduct(final Long seq, final Long menuId, final Long productId, final long quantity,
                       final Price productPrice) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
        this.productPrice = productPrice;
    }

    public BigDecimal calculateAmount() {
        return productPrice.multiply(quantity);
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

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public void setPrice(final Price price) {
        this.productPrice = price;
    }
}
