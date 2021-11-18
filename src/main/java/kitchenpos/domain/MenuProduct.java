package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Product product, final long quantity) {
        this(null, product, quantity);
    }

    public MenuProduct(final Long seq, final Product product, final long quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = new Quantity(quantity);
    }

    public Price getPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity.getValue()));
    }

    public Long getSeq() {
        return seq;
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
