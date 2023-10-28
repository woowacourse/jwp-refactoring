package value;

import exception.InvalidNumberException;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    @Column(name = "quantity", nullable = false)
    private long quantity;

    public Quantity(final long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    protected Quantity() {
    }

    private void validate(final long quantity) {
        if (quantity < 0) {
            throw new InvalidNumberException("수량은 음수가 될 수 없습니다.");
        }
    }

    public long getValue() {
        return this.quantity;
    }
}
