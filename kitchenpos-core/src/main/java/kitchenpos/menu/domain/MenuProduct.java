package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.vo.Quantity;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private Long productId;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    private MenuProduct(
            final Long seq,
            final Long productId,
            final Quantity quantity
    ) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(
            final Long productId,
            final Long quantity
    ) {
        this(null, productId, new Quantity(quantity));
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
