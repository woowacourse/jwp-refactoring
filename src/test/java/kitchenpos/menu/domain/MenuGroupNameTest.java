package kitchenpos.menu.domain;

import kitchenpos.BaseTest;
import kitchenpos.menu.domain.MenuGroupName;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MenuGroupNameTest extends BaseTest {

    @Test
    void 메뉴_그룹을_생성한다() {
        // given
        String name = "beef";

        // when, then
        Assertions.assertThatNoException()
                .isThrownBy(() -> new MenuGroupName(name));
    }

    @Test
    void 메뉴_그룹_이름은_공백일_수_있다() {
        // given
        String name = "";

        // when, then
        Assertions.assertThatNoException()
                .isThrownBy(() -> new MenuGroupName(name));
    }
}
