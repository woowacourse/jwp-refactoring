package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

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
