package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupTest {

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void createMenuGroup() {
        final MenuGroup menuGroup = new MenuGroup("메뉴그룹");

        assertAll(
                () -> assertThat(menuGroup.getId()).isNull(),
                () -> assertThat(menuGroup.getName()).isEqualTo("메뉴그룹")
        );
    }
}
