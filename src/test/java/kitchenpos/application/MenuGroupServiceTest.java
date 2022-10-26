package kitchenpos.application;

import static kitchenpos.application.DomainFixture.getMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest extends ServiceTest {

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        final MenuGroup menuGroup = getMenuGroup();

        final MenuGroup savedMenuGroup = 메뉴_그룹_등록(menuGroup);

        assertAll(
                () -> assertThat(savedMenuGroup.getId()).isNotNull(),
                () -> assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName())
        );
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        메뉴_그룹_등록(getMenuGroup());

        final List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).hasSize(1);
    }
}
