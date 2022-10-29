package kitchenpos.domain;

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

    public MenuProduct(final Product product, final long quantity) {
        this.seq = null;
        this.menu = null;
        this.product = product;
        this.quantity = quantity;
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

    public void setMenu(final Menu menu) {
        this.menu = menu;
    }
}
