package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.menu.dto.request.CreateMenuGroupRequest;
import kitchenpos.menu.dto.response.MenuGroupResponse;

@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("새로운 메뉴 그룹을 생성한다.")
    void create() {
        // given
        CreateMenuGroupRequest request = new CreateMenuGroupRequest("menuGroup");

        // when
        MenuGroupResponse savedMenuGroup = menuGroupService.create(request);

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @Test
    @DisplayName("전체 메뉴 그룹을 조회한다.")
    void list() {
        List<MenuGroupResponse> menuGroups = menuGroupService.list();
        assertThat(menuGroups).isNotNull();
    }
}
