package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupTest {

    @Test
    void 이름_정보가_누락된_메뉴그룹을_생성하려는_경우_예외발생() {
        assertThatThrownBy(() -> new MenuGroup(1L, ""))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
