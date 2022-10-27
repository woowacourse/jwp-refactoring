package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.menuGroup.CreateMenuGroupRequest;

class MenuGroupServiceTest extends ServiceTest {

    @Test
    @DisplayName("새로운 메뉴 그룹을 생성한다.")
    void create() {
        // given
        CreateMenuGroupRequest request = new CreateMenuGroupRequest("menuGroup");

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(request);

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @Test
    @DisplayName("전체 메뉴 그룹을 조회한다.")
    void list() {
        List<MenuGroup> menuGroups = menuGroupService.list();
        assertThat(menuGroups).isNotNull();
    }
}
