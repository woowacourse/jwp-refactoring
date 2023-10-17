package kitchenpos.domain;

public class MenuProduct2 {
    private Long seq;
    private Menu2 menu;
    private Product2 product;
    private long quantity;

    public MenuProduct2(
        final Long seq,
        final Menu2 menu,
        final Product2 product,
        final long quantity
    ) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct2(final Menu2 menu, final Product2 product, final long quantity) {
        this(null, menu, product, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Menu2 getMenu() {
        return menu;
    }

    public Product2 getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
