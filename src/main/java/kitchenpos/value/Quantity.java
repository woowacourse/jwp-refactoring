package kitchenpos.value;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.InvalidNumberException;

@Embeddable
public class Quantity {

    @Column(name = "quantity", nullable = false)
    private long quantity;

    public Quantity(long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    protected Quantity() {
    }

    private void validate(final long quantity){
        if(quantity < 0){
            throw new InvalidNumberException("수량은 음수가 될 수 없습니다.");
        }
    }

    public long getValue(){
        return this.quantity;
    }
}
