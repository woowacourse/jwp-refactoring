package kitchenpos.domain;

import kitchenpos.exception.FieldNotValidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {
    @DisplayName("메뉴를 생성한다. - 실패, name이 null이거나 empty")
    @ParameterizedTest
    @NullAndEmptySource
    void createWithNotValidName(String name) {
        // given - when
        assertThatThrownBy(() -> new Menu(name, BigDecimal.TEN))
                .isInstanceOf(FieldNotValidException.class);
    }

    @DisplayName("메뉴를 생성한다. - 실패, 가격이 음수")
    @Test
    void createWithNotValidPrice() {
        // given - when
        assertThatThrownBy(() -> new Menu("joanne", BigDecimal.valueOf(-10)))
                .isInstanceOf(FieldNotValidException.class);
    }

    @DisplayName("메뉴를 생성한다. - 실패, 가격이 null")
    @Test
    void createWithNullPrice() {
        // given - when
        assertThatThrownBy(() -> new Menu("joanne", null))
                .isInstanceOf(FieldNotValidException.class);
    }
}
