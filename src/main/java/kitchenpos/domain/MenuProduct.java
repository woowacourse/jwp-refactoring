package kitchenpos.domain;

import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;

public class MenuProduct {

    @Id
    private final Long seq;
    private final Long productId;
    private final long quantity;

    @Transient
    private Price price;

    public MenuProduct(final Long productId, final long quantity, final BigDecimal price) {
        this.seq = null;
        this.productId = productId;
        this.quantity = quantity;
        this.price = new Price(price);
    }

    @PersistenceCreator
    private MenuProduct(final Long seq, final Long productId, final long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public BigDecimal calculateAmount() {
        return price.multiply(quantity);
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
