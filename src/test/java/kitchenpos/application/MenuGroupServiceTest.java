package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MenuGroupServiceTest {

    private MenuGroupService menuGroupService;

    @Autowired
    public MenuGroupServiceTest(MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @Test
    void create() {
        // given
        MenuGroupRequest request = new MenuGroupRequest("메뉴그룹");
        // when
        MenuGroup createdMenuGroup = menuGroupService.create(request);
        // then
        assertThat(createdMenuGroup.getId()).isNotNull();
    }

    @Test
    void list() {
        // given & when
        List<MenuGroup> menuGroups = menuGroupService.list();
        // then
        int defaultSize = 4;
        assertThat(menuGroups).hasSize(defaultSize);
    }
}
