package kitchenpos.menu.domain;

import java.math.BigDecimal;
import kitchenpos.common.Price;

public class MenuProduct {
    private Long seq;
    private Long menuId;
    private final Long productId;
    private final long quantity;
    private transient final Price price;

    public MenuProduct(Long seq, Long menuId, Long productId, long quantity, Price price) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public MenuProduct(Long productId, long quantity, Price price) {
        this(null, null, productId, quantity, price);
    }

    public void placeMenuId(final Long menuId) {
        this.menuId = menuId;
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
        return price.getPrice();
    }

    public void placeSeq(Long seq) {
        this.seq = seq;
    }
}
