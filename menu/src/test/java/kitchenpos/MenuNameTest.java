package kitchenpos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuNameTest {
    @DisplayName("메뉴 이름이 비어 있거나 공백이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void validateMenuNameBlank(String value) {
        assertThatThrownBy(() -> new MenuName(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 이름은 공백이 될 수 없습니다.");
    }

    @Test
    @DisplayName("메뉴 이름이 null이면 예외가 발생한다.")
    void validateMenuNameNull() {
        assertThatThrownBy(() -> new MenuName(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 이름은 공백이 될 수 없습니다.");
    }
}
