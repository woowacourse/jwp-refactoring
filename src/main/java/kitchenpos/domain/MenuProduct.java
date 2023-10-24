package kitchenpos.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long seq;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private Quantity quantity;

    public MenuProduct(Long seq, Product product, Quantity quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(Product product, Quantity quantity) {
        this(null, product, quantity);
    }

    protected MenuProduct() {
    }

    public static MenuProduct of(Product product, long quantity) {
        return new MenuProduct(product, Quantity.valueOf(quantity));
    }

    public BigDecimal calculateTotalPrice() {
        return product.calculateTotalPrice(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return product.getId();
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public long getQuantityValue() {
        return quantity.getValue();
    }
}
