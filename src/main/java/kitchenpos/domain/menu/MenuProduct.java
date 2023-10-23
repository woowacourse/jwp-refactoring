package kitchenpos.domain.menu;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long productId;
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(
            final Long seq,
            final Long productId,
            final long quantity
    ) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(final Long productId, final long quantity) {
        this(null, productId, quantity);
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
