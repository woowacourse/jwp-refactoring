package kitchenpos.domain.menuproduct;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.InvalidQuantityException;

@Embeddable
public class MenuProductQuantity {

    private static final int MINIMUM_QUANTITY = 0;

    @Column(name = "quantity", nullable = false)
    private Long value;

    protected MenuProductQuantity() {
    }

    public MenuProductQuantity(final long value) {
        validate(value);
        this.value = value;
    }

    private void validate(final long value) {
        validatePositive(value);
    }

    private static void validatePositive(final long value) {
        if (value < MINIMUM_QUANTITY) {
            throw new InvalidQuantityException("메뉴 상품 수량은 0개 이상이어야 합니다.");
        }
    }

    public long getValue() {
        return value;
    }
}
