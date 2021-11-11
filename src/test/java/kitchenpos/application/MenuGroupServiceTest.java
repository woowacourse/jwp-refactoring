package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupServiceTest extends IntegrationTest {

    @Test
    void create() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("MenuGroup");

        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertThat(savedMenuGroup.getId()).isNotNull();
        assertThat(savedMenuGroup.getName()).isEqualTo("MenuGroup");
    }

    @Test
    void list() {
        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).hasSize(4);
    }
}