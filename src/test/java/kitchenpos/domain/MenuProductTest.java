package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuProductTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, -10})
    @DisplayName("0 이하의 quantity를 적용할 경우 예외를 발생시킨다.")
    void negativeQuantity(int quantity) {
        assertThatThrownBy(() -> new MenuProduct(1L, 1L, 1L, quantity))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("0 이하의 양을 등록할 수 없습니다.");
    }
}