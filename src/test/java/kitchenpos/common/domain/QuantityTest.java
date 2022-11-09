package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.exception.EmptyDataException;
import kitchenpos.common.exception.InvalidQuantityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuantityTest {

    @DisplayName("null로 Quantity를 생성하려고 하면 예외를 발생시킨다.")
    @Test
    void from_Exception_Null() {
        assertThatThrownBy(() -> new Quantity(null))
                .isInstanceOf(EmptyDataException.class)
                .hasMessageContaining(Quantity.class.getSimpleName())
                .hasMessageContaining("입력되지 않았습니다");
    }

    @DisplayName("1보다 작은 값으로 Quantity를 생성하려고 하면 예외를 발생시킨다.")
    @Test
    void from_Exception_InvalidQuantity() {
        Long invalidQuantity = -1L;
        assertThatThrownBy(() -> new Quantity(invalidQuantity))
                .isInstanceOf(InvalidQuantityException.class);
    }
}