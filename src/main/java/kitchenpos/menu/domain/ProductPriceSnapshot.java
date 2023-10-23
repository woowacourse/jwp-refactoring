package kitchenpos.menu.domain;

import java.math.BigDecimal;
import javax.persistence.Embeddable;

@Embeddable
public class ProductPriceSnapshot {

    private BigDecimal value;

    protected ProductPriceSnapshot() {
    }

    public ProductPriceSnapshot(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
