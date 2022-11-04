package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import kitchenpos.vo.Price;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @NotNull
    private Long productId;

    @NotNull
    private long quantity;

    @Transient
    private Price productPrice;

    protected MenuProduct() {
    }

    public MenuProduct(final Long productId, final long quantity, final Price productPrice) {
        this(null, productId, quantity, productPrice);
    }

    public MenuProduct(final Long seq, final Long productId, final long quantity,
                       final Price productPrice) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
        this.productPrice = productPrice;
    }

    public BigDecimal calculateAmount() {
        return productPrice.multiply(quantity);
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

    public void setPrice(final Price price) {
        this.productPrice = price;
    }
}
