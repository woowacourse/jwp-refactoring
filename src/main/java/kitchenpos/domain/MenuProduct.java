package kitchenpos.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Product product, final Quantity quantity) {
        this(null, product, quantity);
    }

    public MenuProduct(final Long seq, final Product product, final Quantity quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public Price calculateAmount() {
        return this.product.multiplyPriceWith(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity.getValue();
    }
}
