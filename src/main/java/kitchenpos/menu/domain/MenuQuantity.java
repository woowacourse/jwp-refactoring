package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import kitchenpos.menu.exception.InvalidMenuQuantityException;

@Embeddable
public class MenuQuantity {

    @NotNull
    @Column(name = "quantity")
    private Long value;

    protected MenuQuantity() {
    }

    public MenuQuantity(Long value) {
        this.value = value;
        validateNull(this.value);
        validateNegative(this.value);
    }

    private void validateNull(Long value) {
        if (Objects.isNull(value)) {
            throw new InvalidMenuQuantityException("수량은 null 일 수 없습니다.");
        }
    }

    private void validateNegative(Long value) {
        if (value < 0) {
            throw new InvalidMenuQuantityException(String.format("음수 %s는 개수가 될 수 없습니다.", value));
        }
    }

    public BigDecimal getDecimalValue() {
        return BigDecimal.valueOf(value);
    }

    public Long getValue() {
        return value;
    }
}
