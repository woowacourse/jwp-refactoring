package kitchenpos.domain;

import static kitchenpos.application.fixture.MenuFixture.MENU_NAME;
import static kitchenpos.application.fixture.MenuFixture.MENU_PRODUCTS;
import static kitchenpos.application.fixture.MenuGroupFixture.MENU_GROUP_ID;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("메뉴의 가격은 0 이상이여야 한다. 그렇지 않으면 예외가 발생한다.")
    @Test
    void constructor_Exception_Invalid_Price() {
        assertThatThrownBy(() -> new Menu(MENU_NAME, -1, MENU_GROUP_ID, MENU_PRODUCTS))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
