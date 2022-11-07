package kitchenpos.domain.menu;

import java.math.BigDecimal;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.Price;
import kitchenpos.domain.Quantity;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long productId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "product_price"))
    private Price productPrice;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Long productId, final BigDecimal productPrice, final long quantity) {
        this(null, productId, new Price(productPrice), new Quantity(quantity));
    }

    public MenuProduct(final Long seq, final Long productId, final Price productPrice, final Quantity quantity) {
        this.seq = seq;
        this.productId = productId;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public Price calculateAmount() {
        return this.productPrice.multiply(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public Price getProductPrice() {
        return productPrice;
    }

    public long getQuantity() {
        return quantity.getValue();
    }
}
