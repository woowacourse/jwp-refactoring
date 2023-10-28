package kitchenpos.menu.domain;

import kitchenpos.common.vo.Price;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long productId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price"))
    private Price price;

    private long quantity;

    protected MenuProduct() {}

    public MenuProduct(final Long productId, final Price price, final long quantity) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    public Price calculatePrice() {
        final BigDecimal multiply = price.getValue().multiply(BigDecimal.valueOf(quantity));

        return new Price(multiply);
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MenuProduct that = (MenuProduct) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

    @Override
    public String toString() {
        return "MenuProduct{" +
               "seq=" + seq +
               ", quantity=" + quantity +
               '}';
    }
}
