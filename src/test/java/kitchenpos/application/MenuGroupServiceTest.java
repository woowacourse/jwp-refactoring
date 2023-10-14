package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends IntegrationTest {

    @Autowired
    private MenuGroupService menuGroupService;


    @Test
    void create() {
        // given
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("Chicken-group");

        // when
        final MenuGroup createdMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(createdMenuGroup.getId()).isNotNull();
    }

    @Test
    void list() {
        // given
        final MenuGroup chickenGroup = new MenuGroup();
        chickenGroup.setName("Chicken-group");
        menuGroupService.create(chickenGroup);
        final MenuGroup pizzaGroup = new MenuGroup();
        pizzaGroup.setName("Pizza-group");
        menuGroupService.create(pizzaGroup);

        // when
        final List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).hasSize(2);
    }
}
