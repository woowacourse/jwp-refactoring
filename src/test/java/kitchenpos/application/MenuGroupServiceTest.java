package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.dto.request.MenuGroupRequest;
import kitchenpos.menu.application.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class MenuGroupServiceTest extends ApplicationTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 조회한다.")
    @Test
    void list() {
        MenuGroupRequest menuGroupRequest1 = new MenuGroupRequest("치킨 메뉴");
        MenuGroupRequest menuGroupRequest2 = new MenuGroupRequest("보족 메뉴");

        Long menuGroupId1 = menuGroupService.create(menuGroupRequest1);
        Long menuGroupId2 = menuGroupService.create(menuGroupRequest2);

        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        assertThat(menuGroups).extracting(MenuGroupResponse::getId, MenuGroupResponse::getName)
                .containsExactlyInAnyOrder(tuple(menuGroupId1, "치킨 메뉴"),
                        tuple(menuGroupId2, "보족 메뉴"));
    }
}
