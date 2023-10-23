package kitchenpos.menugroup.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.test.fixtures.MenuGroupFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuGroupServiceTest {
    @Autowired
    MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 등록한다")
    void createMenuGroup() {
        // given
        final MenuGroup menuGroup = MenuGroupFixtures.BASIC.get();

        // when
        final MenuGroup saved = menuGroupService.create(menuGroup);

        // then
        assertThat(saved.name()).isEqualTo(menuGroup.name());
    }

    @Test
    @DisplayName("메뉴 그룹 목록 조회")
    void getMenuGroups() {
        // given
        menuGroupService.create(MenuGroupFixtures.BASIC.get());

        // when
        final List<MenuGroup> actualMenuGroups = menuGroupService.list();

        // then
        assertThat(actualMenuGroups).isNotEmpty();
    }
}
