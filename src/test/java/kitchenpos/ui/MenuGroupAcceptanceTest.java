package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;

class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    void create() {
        MenuGroup menuGroup = new MenuGroup("menu-group");
        MenuGroup created = makeResponse("/api/menu-groups/", TestMethod.POST, menuGroup)
            .as(MenuGroup.class);

        assertAll(
            () -> assertThat(created.getId()).isNotNull(),
            () -> assertThat(created.getName()).isEqualTo(menuGroup.getName())
        );

        System.out.println(created.getId());
    }

    @Test
    void list() {
        MenuGroup menuGroup1 = new MenuGroup("menu-group1");
        MenuGroup menuGroup2 = new MenuGroup("menu-group2");
        makeResponse("/api/menu-groups/", TestMethod.POST, menuGroup1);
        makeResponse("/api/menu-groups/", TestMethod.POST, menuGroup2);

        List<MenuGroup> menuGroups = makeResponse("/api/menu-groups/", TestMethod.GET).jsonPath()
            .getList(".", MenuGroup.class);

        assertAll(
            () -> assertThat(menuGroups.size()).isEqualTo(2),
            () -> assertThat(menuGroups.stream()
                .map(MenuGroup::getName).collect(Collectors.toList()))
                .containsExactly(menuGroup1.getName(), menuGroup2.getName())
        );
    }
}