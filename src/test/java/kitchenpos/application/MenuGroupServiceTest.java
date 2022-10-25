package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 조회한다.")
    @Test
    void list() {
        MenuGroup menuGroup1 = givenMenuGroup("치킨 메뉴");
        MenuGroup menuGroup2 = givenMenuGroup("보족 메뉴");

        MenuGroup savedMenuGroup1 = menuGroupService.create(menuGroup1);
        MenuGroup savedMenuGroup2 = menuGroupService.create(menuGroup2);

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).extracting(MenuGroup::getId, MenuGroup::getName)
                .containsExactlyInAnyOrder(
                        tuple(savedMenuGroup1.getId(), "치킨 메뉴"),
                        tuple(savedMenuGroup2.getId(), "보족 메뉴")
                );
    }

    private MenuGroup givenMenuGroup(final String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}