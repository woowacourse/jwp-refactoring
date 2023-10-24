package kitchenpos.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class MenuNameTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 255})
    @DisplayName("메뉴 이름을 생성할 수 있다.")
    void 메뉴_이름_성공(int nameLength) {
        // given
        final String name = "a".repeat(nameLength);

        // when
        final MenuName menuName = MenuName.from(name);

        // then
        assertThat(menuName.getName())
                .isEqualTo(name);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("메뉴 이름은 null이거나 빈 문자열일 수 없다.")
    void 메뉴_이름_생성_실패_없는값(String name) {
        assertThatThrownBy(() -> MenuName.from(name))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 이름의 최대 길이는 255자이다.")
    void 메뉴_이름_생성_실패_길이초과() {
        // given
        String tooLongName = "a".repeat(256);

        // expected
        assertThatThrownBy(() -> MenuName.from(tooLongName))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
