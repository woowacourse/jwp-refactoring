package kitchenpos.menu;

import kitchenpos.menu.domain.MenuName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuNameTest {
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 255})
    void 메뉴_이름은_255자_이하이다(int length) {
        final MenuName menuName = new MenuName("메".repeat(length));
        assertThat(menuName.getName()).isEqualTo("메".repeat(length));
    }

    @Test
    void 메뉴_이름이_없으면_예외가_발생한다() {
        assertThatThrownBy(() -> new MenuName(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_이름이_256자_이상이면_예외가_발생한다() {
        assertThatThrownBy(() -> new MenuName("메".repeat(256)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
