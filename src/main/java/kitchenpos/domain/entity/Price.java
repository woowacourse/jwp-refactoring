package kitchenpos.domain.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price")
    private long value;

    protected Price() {
    }

    public Price(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
