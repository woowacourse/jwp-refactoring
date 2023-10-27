package kitchenpos.domain;

import kitchenpos.common.BaseDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class MenuProduct extends BaseDate {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    private long quantity;

    public MenuProduct(final Long seq, final Product product, final long quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(final Product product, final long quantity) {
        this(null, product, quantity);
    }

    protected MenuProduct() {
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

    @Override
    public String toString() {
        return "MenuProduct{" +
                "seq=" + seq +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
