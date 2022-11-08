package kitchenpos.menu.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

public class MenuProduct {

    @Id
    private final Long seq;
    private final Long productId;
    private final long quantity;

    public MenuProduct(final Long productId, final long quantity) {
        this(null, productId, quantity);
    }

    @PersistenceCreator
    private MenuProduct(final Long seq, final Long productId, final long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
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
