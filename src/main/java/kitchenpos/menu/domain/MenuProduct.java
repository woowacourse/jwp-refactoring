package kitchenpos.menu.domain;

import kitchenpos.common.BaseDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class MenuProduct extends BaseDate {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private Long productId;
    private long quantity;

    public MenuProduct(final Long seq, final Long productId, final long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(final Long productId, final long quantity) {
        this(null, productId, quantity);
    }

    protected MenuProduct() {
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
}
