package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.EmptyDataException;
import kitchenpos.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {

    @DisplayName("null로 Price를 생성하려고 하면 예외를 발생시킨다.")
    @Test
    void from_Exception_Null() {
        assertThatThrownBy(() -> Price.from(null))
                .isInstanceOf(EmptyDataException.class)
                .hasMessageContaining(Price.class.getSimpleName())
                .hasMessageContaining("입력되지 않았습니다.");
    }

    @DisplayName("0보다 작은 값으로 Price를 생성하려고 하면 예외를 발생시킨다.")
    @Test
    void from_Exception_InvalidPrice() {
        double invalidPrice = -0.1;

        assertThatThrownBy(() -> Price.from(invalidPrice))
                .isInstanceOf(InvalidPriceException.class);
    }
}
