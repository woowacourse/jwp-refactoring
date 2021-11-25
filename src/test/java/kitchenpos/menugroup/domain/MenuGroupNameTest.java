package kitchenpos.menugroup.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.menugroup.exception.InvalidMenuGroupNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MenuGroupName 단위 테스트")
class MenuGroupNameTest {

    @DisplayName("MenuGroupName을 생성할 때")
    @Nested
    class Create {

        @DisplayName("값이 null일 경우 예외가 발생한다.")
        @Test
        void nullException() {
            // when, then
            assertThatThrownBy(() -> new MenuGroupName(null))
                .isExactlyInstanceOf(InvalidMenuGroupNameException.class);
        }

        @DisplayName("값이 공백으로만 이루어진 경우 예외가 발생한다.")
        @Test
        void blankException() {
            // when, then
            assertThatThrownBy(() -> new MenuGroupName(" "))
                .isExactlyInstanceOf(InvalidMenuGroupNameException.class);
        }
    }
}
