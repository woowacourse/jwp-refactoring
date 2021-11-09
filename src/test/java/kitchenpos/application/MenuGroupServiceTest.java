package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import kitchenpos.SpringBootTestWithProfiles;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.service.MenuGroupRequest;
import kitchenpos.menugroup.service.MenuGroupResponse;
import kitchenpos.menugroup.service.MenuGroupService;
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
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("menu_group");
        MenuGroupResponse created = menuGroupService.create(menuGroupRequest);

        assertNotNull(created.getId());
        assertThat(created.getName()).isEqualTo(menuGroupRequest.getName());
    }

    @Test
    void list() {
        menuGroupService.create(new MenuGroupRequest("group1"));
        menuGroupService.create(new MenuGroupRequest("group2"));
        menuGroupService.create(new MenuGroupRequest("group3"));

        List<MenuGroupResponse> menuGroups = menuGroupService.list();
        assertThat(menuGroups.size()).isEqualTo(3);
    }

    @AfterEach
    void tearDown() {
        menuGroupRepository.deleteAllInBatch();
    }
}
