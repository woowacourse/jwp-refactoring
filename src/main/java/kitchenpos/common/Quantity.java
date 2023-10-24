package kitchenpos.common;

import javax.persistence.Column;

public class Quantity {

    @Column(name = "quantity")
    private int value;

    protected Quantity() {
    }

    public Quantity(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("수량은 음수 일 수 없습니다.");
        }
    }

    public int getValue() {
        return value;
    }
}
