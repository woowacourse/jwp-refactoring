package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.request.MenuGroupCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성 후 조회한다.")
    @Test
    void createAndList() {
        // given
        MenuGroupCreateRequest request = new MenuGroupCreateRequest("두마리메뉴");

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(request);
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).hasSize(1);
        assertThat(menuGroups.get(0).getName()).isEqualTo(savedMenuGroup.getName());
    }
}
