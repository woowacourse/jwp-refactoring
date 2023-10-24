package kitchenpos.domain.value;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    public Price(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    protected Price() {
    }

    private void validate(final BigDecimal price){
        if(Objects.isNull(price) ||price.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal getPrice(){
        return this.price;
    }

    public BigDecimal multiply(final Quantity quantity){
        return this.price.multiply(BigDecimal.valueOf(quantity.getValue()));
    }

    public void isValidPrice(final BigDecimal sum){
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }
}
