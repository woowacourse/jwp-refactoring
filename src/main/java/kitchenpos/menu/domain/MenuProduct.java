package kitchenpos.menu.domain;

import javax.persistence.*;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = new Quantity(quantity);
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
