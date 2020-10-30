package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.MENU_GROUP_FIXTURE_고기;
import static kitchenpos.fixture.MenuGroupFixture.MENU_GROUP_FIXTURE_국;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void create() {
        MenuGroup menuGroup = MENU_GROUP_FIXTURE_고기;

        MenuGroup persistMenuGroup = menuGroupService.create(menuGroup);

        assertThat(persistMenuGroup.getId()).isNotNull();
    }

    @Test
    void list() {
        menuGroupService.create(MENU_GROUP_FIXTURE_고기);
        menuGroupService.create(MENU_GROUP_FIXTURE_국);

        List<MenuGroup> menuGroups = menuGroupService.list();
        List<String> menuGroupNames = menuGroups.stream()
            .map(MenuGroup::getName)
            .collect(Collectors.toList());

        assertThat(menuGroupNames).contains(MENU_GROUP_FIXTURE_고기.getName(), MENU_GROUP_FIXTURE_국.getName());
    }
}