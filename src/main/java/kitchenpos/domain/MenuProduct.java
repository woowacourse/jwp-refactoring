package kitchenpos.domain;

import java.math.BigDecimal;

public class MenuProduct {
    private Long seq;
    private Menu menu;
    private Product product;
    private long quantity;

    public MenuProduct(final Long seq, final Menu menu, final Product product, final long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(final Menu menu, final Product product, final long quantity) {
        this(null, menu, product, quantity);
    }

    public BigDecimal calculateTotalPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public Long getProductId() {
        return product.getId();
    }

    public long getQuantity() {
        return quantity;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }
}
