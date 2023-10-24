package kitchenpos.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    @Column(name = "quantity", nullable = false)
    private long value;

    private Quantity(long value) {
        this.value = value;
    }

    protected Quantity() {
    }

    public static Quantity valueOf(long value) {
        return new Quantity(value);
    }

    public long getValue() {
        return value;
    }
}
