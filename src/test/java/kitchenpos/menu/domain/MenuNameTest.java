package kitchenpos.menu.domain;

import kitchenpos.BaseTest;
import kitchenpos.menu.domain.MenuName;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MenuNameTest extends BaseTest {

    @Test
    void 메뉴_이름을_생성한다() {
        // given
        String name = "pizza";

        // when, then
        Assertions.assertThatNoException()
                .isThrownBy(() -> new MenuName(name));
    }

    @Test
    void 메뉴_이름은_공백일_수_있다() {
        // given
        String name = "";

        // when, then
        Assertions.assertThatNoException()
                .isThrownBy(() -> new MenuName(name));
    }
}
