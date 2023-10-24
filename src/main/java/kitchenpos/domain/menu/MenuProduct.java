package kitchenpos.domain.menu;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import kitchenpos.domain.product.Product;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    private Product product;

    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Product product, final long quantity) {
        this(null, product, quantity);
    }

    public MenuProduct(final Long seq, final Product product, final long quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public BigDecimal calculatePrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
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
