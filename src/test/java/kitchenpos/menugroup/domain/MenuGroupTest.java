package kitchenpos.menugroup.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.menugroup.exception.InvalidNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MenuGroup 단위 테스트")
public class MenuGroupTest {

    @DisplayName("MenuGroup을 생성할 때")
    @Nested
    class Create {

        @DisplayName("name이 Null인 경우 예외가 발생한다.")
        @Test
        void nameNullException() {
            // when, then
            assertThatThrownBy(() -> new MenuGroup(null))
                .isExactlyInstanceOf(InvalidNameException.class);
        }

        @DisplayName("name이 공백뿐인 경우 예외가 발생한다.")
        @Test
        void nameBlankException() {
            // when, then
            assertThatThrownBy(() -> new MenuGroup(" "))
                .isExactlyInstanceOf(InvalidNameException.class);
        }
    }
}
