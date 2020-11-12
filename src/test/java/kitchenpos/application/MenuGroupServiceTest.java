package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.MENU_GROUP_FIXTURE_1;
import static kitchenpos.fixture.MenuGroupFixture.MENU_GROUP_FIXTURE_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void create() {
        MenuGroupRequest menuGroup = new MenuGroupRequest("고기");

        MenuGroupResponse persistMenuGroup = menuGroupService.create(menuGroup);

        assertAll(
            () -> assertThat(persistMenuGroup.getId()).isNotNull(),
            () -> assertThat(persistMenuGroup.getName()).isEqualTo(menuGroup.getName())
        );
    }

    @Test
    void list() {
        menuGroupService.create(new MenuGroupRequest("고기"));
        menuGroupService.create(new MenuGroupRequest("국"));

        List<MenuGroupResponse> menuGroups = menuGroupService.list();
        List<String> menuGroupNames = menuGroups.stream()
            .map(MenuGroupResponse::getName)
            .collect(Collectors.toList());

        assertThat(menuGroupNames).contains(MENU_GROUP_FIXTURE_1.getName(), MENU_GROUP_FIXTURE_2.getName());
    }
}