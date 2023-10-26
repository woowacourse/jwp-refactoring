package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

public class MenuProduct {

    private final Long seq;
    private final Product product;
    private final long quantity;

    public MenuProduct(final Long seq, final Product product, final long quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(final Product product, final long quantity) {
        this(null, product, quantity);
    }

    public Price calculatePrice() {
        return product.getPrice().multiply(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }

}
