package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;

import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupTest {

    @Test
    void 메뉴_그룹_이름을_받아서_메뉴_그룹_정보를_등록할_수_있다() {
        //given
        Long id = 1L;
        String name = "추천메뉴";

        //when, then
        assertThatNoException().isThrownBy(() -> new MenuGroup(id, name));
    }
}
