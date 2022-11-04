package kitchenpos.menu.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import kitchenpos.domain.Price;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private long quantity;

    @Transient
    private Price price;

    protected MenuProduct() {
    }

    public MenuProduct(final Long productId, final long quantity, final BigDecimal price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = new Price(price);
    }

    public BigDecimal getAmount() {
        return price.multiply(BigDecimal.valueOf(quantity));
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

    public Price getPrice() {
        return price;
    }
}
