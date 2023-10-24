package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupTest {

    @Test
    @DisplayName("메뉴 그룹에는 이름이 있어야 한다.")
    void 메뉴_그룹_생성_실패_이름_없음() {
        // given
        final String nullName = null;

        // expected
        assertThatThrownBy(() -> new MenuGroup(null))
                .isInstanceOf(Exception.class);
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 256})
    @DisplayName("메뉴 그룹 이름의 길이는 최소 1자, 최대 255자이다.")
    void 메뉴_그룹_생성_실패_이름_길이(final int nameLength) {
        // given
        final String name = "a".repeat(nameLength);

        // expected
        assertThatThrownBy(() -> new MenuGroup(name))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
