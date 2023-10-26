package kitchenpos.domain.menu;

import kitchenpos.exception.KitchenposException;

import javax.persistence.Embeddable;

import static kitchenpos.exception.ExceptionInformation.MENU_QUANTITY_OUT_OF_BOUNCE;

@Embeddable
public class Quantity {

    private static final int MIN_QUANTITY_BOUND = 0;

    private long quantity;

    public Quantity() {
    }

    public Quantity(final long quantity) {
        this.quantity = quantity;
    }

    public static Quantity create(final long quantity){
        validateBound(quantity);
        return new Quantity(quantity);
    }

    private static void validateBound(final long quantity) {
        if(quantity < MIN_QUANTITY_BOUND){
            throw new KitchenposException(MENU_QUANTITY_OUT_OF_BOUNCE);
        }
    }

    public long getQuantity() {
        return quantity;
    }
}
