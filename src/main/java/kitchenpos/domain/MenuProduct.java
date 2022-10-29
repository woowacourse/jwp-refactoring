package kitchenpos.domain;

import java.math.BigDecimal;

public class MenuProduct {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;
    private Price price;

    public MenuProduct() {
    }

    public MenuProduct(final Long menuId, final Long productId, final long quantity, final BigDecimal price) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = new Price(price);
    }

    public MenuProduct(final Long productId, final long quantity, final BigDecimal price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = new Price(price);
    }

    public BigDecimal getAmount() {
        return price.multiply(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
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

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
