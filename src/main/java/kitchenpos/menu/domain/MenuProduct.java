package kitchenpos.menu.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column
    private Long productId;

    @Column
    private long quantity;

    @Transient
    private BigDecimal price;

    protected MenuProduct() {
    }

    public MenuProduct(final Long productId, final long quantity, final BigDecimal price) {
        this(null, productId, quantity, price);
    }

    public MenuProduct(final Long seq, final Long productId, final long quantity, final BigDecimal price) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public BigDecimal calculateAmount() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
