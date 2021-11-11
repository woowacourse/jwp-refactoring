package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import kitchenpos.SpringBootTestWithProfiles;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTestWithProfiles
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    void create() {
        MenuGroup menuGroup = new MenuGroup("menu_group");
        MenuGroupResponse created = menuGroupService.create(menuGroup);

        assertNotNull(created.getId());
        assertThat(created.getName()).isEqualTo(menuGroup.getName());
    }

    @Test
    void list() {
        menuGroupService.create(new MenuGroup("group1"));
        menuGroupService.create(new MenuGroup("group2"));
        menuGroupService.create(new MenuGroup("group3"));

        List<MenuGroupResponse> menuGroups = menuGroupService.list();
        assertThat(menuGroups.size()).isEqualTo(3);
    }

    @AfterEach
    void tearDown() {
        menuGroupRepository.deleteAllInBatch();
    }
}
