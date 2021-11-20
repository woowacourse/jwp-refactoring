package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.InvalidNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Name 단위 테스트")
class NameTest {

    @DisplayName("Name을 생성할 때")
    @Nested
    class Create {

        @DisplayName("값이 null일 경우 예외가 발생한다.")
        @Test
        void nullException() {
            // when, then
            assertThatThrownBy(() -> new Name(null))
                .isExactlyInstanceOf(InvalidNameException.class);
        }

        @DisplayName("값이 공백으로만 이루어진 경우 예외가 발생한다.")
        @Test
        void blankException() {
            // when, then
            assertThatThrownBy(() -> new Name(" "))
                .isExactlyInstanceOf(InvalidNameException.class);
        }
    }
}
