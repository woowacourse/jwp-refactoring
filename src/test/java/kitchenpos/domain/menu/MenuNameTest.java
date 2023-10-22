package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.MenuException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MenuNameTest {

    @Test
    void 메뉴_이름은_공백일_수_없다() {
        // given
        final String menuName = " ";

        // expected
        assertThatThrownBy(() -> new MenuName(menuName))
                .isInstanceOf(MenuException.NoMenuNameException.class);
    }

    @Test
    void 메뉴_이름은_없을_수_없다() {
        // given
        final String menuName = null;

        // expected
        assertThatThrownBy(() -> new MenuName(menuName))
                .isInstanceOf(MenuException.NoMenuNameException.class);
    }
}
