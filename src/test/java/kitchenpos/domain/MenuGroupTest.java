package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupTest {

    @Test
    void createMenuGroup() {
        // given
        String name = "메뉴그룹";
        // when
        MenuGroup menuGroup = new MenuGroup(name);
        // then
        assertThat(menuGroup).isNotNull();
    }
}
