package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import kitchenpos.SpringBootTestWithProfiles;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTestWithProfiles
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void create() {
        MenuGroup menuGroup = new MenuGroup("menu_group");
        MenuGroup created = menuGroupService.create(menuGroup);

        assertNotNull(created.getId());
        assertThat(created.getName()).isEqualTo(menuGroup.getName());
    }

    @Test
    void list() {
        menuGroupService.create(new MenuGroup("group1"));
        menuGroupService.create(new MenuGroup("group2"));
        menuGroupService.create(new MenuGroup("group3"));

        List<MenuGroup> menuGroups = menuGroupService.list();
        assertThat(menuGroups.size()).isEqualTo(3);
    }
}
