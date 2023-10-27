package domain;

import exception.InvalidQuantityException;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuProductQuantity {

    private static final int MINIMUM_QUANTITY = 1;

    @Column(name = "quantity", nullable = false)
    private Long value;

    protected MenuProductQuantity() {
    }

    public MenuProductQuantity(final Long value) {
        validate(value);
        this.value = value;
    }

    private void validate(final Long value) {
        validatePositive(value);
    }

    private static void validatePositive(final Long value) {
        if (value < MINIMUM_QUANTITY) {
            throw new InvalidQuantityException("메뉴 상품 수량은 1개 이상이어야 합니다.");
        }
    }

    public long getValue() {
        return value;
    }
}
