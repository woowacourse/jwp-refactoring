package kitchenpos.domain;

public class MenuProduct2 {
    private Long seq;
    private Product2 product;
    private long quantity;

    public MenuProduct2(
        final Long seq,
        final Product2 product,
        final long quantity
    ) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct2(final Product2 product, final long quantity) {
        this(null,product, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Product2 getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
