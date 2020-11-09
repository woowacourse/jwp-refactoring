package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import kitchenpos.dto.menugroup.MenuGroupRequest;
import kitchenpos.dto.menugroup.MenuGroupResponse;

@Import(MenuGroupService.class)
class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("create: 메뉴 그룹 생성")
    @Test
    void create() {
        final MenuGroupRequest menuGroupRequest = new MenuGroupRequest("이십마리메뉴");

        final MenuGroupResponse actual = menuGroupService.create(menuGroupRequest);

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("이십마리메뉴")
        );
    }

    @DisplayName("list: 메뉴 그룹 전체 조회")
    @Test
    void list() {
        final MenuGroupRequest menuGroupRequest1 = new MenuGroupRequest("이십마리메뉴");
        final MenuGroupRequest menuGroupRequest2 = new MenuGroupRequest("삼십마리메뉴");
        menuGroupService.create(menuGroupRequest1);
        menuGroupService.create(menuGroupRequest2);

        final List<MenuGroupResponse> actual = menuGroupService.list();

        assertThat(actual).hasSize(2);
    }
}