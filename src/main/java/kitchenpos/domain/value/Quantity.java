package kitchenpos.domain.value;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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
            throw new IllegalArgumentException();
        }
    }

    public long getValue(){
        return this.quantity;
    }
}
