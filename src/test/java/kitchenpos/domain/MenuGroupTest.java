package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.badrequest.MenuGroupNameInvalidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class MenuGroupTest {
    @DisplayName("MenuGroup은")
    @Nested
    class MenuGroupConstructor {
        @DisplayName("name으로 생성할 수 있다")
        @Test
        void success_to_create() {
            // given
            final var name = "두 마리 메뉴";

            // when
            final var actual = new MenuGroup(name);

            // then
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(new MenuGroup(null, name));
        }

        @DisplayName("name이 null이거나 비어있을 경우 예외가 발생한다")
        @NullAndEmptySource
        @ParameterizedTest
        void creating_menuGroup_with_null_or_empty_name_should_fail(final String invalidName) {
            // given
            final var name = invalidName;

            // when & then
            assertThatThrownBy(() -> new MenuGroup(name))
                    .isInstanceOf(MenuGroupNameInvalidException.class);
        }
    }
}
