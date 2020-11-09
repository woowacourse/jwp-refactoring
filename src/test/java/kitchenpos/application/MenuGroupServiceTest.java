package kitchenpos.application;

import static kitchenpos.fixture.RequestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.MenuGroupResponse;

@Transactional
@SpringBootTest
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 생성")
    @Test
    void create() {
        Long menuId = menuGroupService.create(MENU_GROUP_REQUEST).getId();

        assertThat(menuId).isNotNull();
    }

    @DisplayName("메뉴 그룹 전체 조회")
    @Test
    void list() {
        menuGroupService.create(MENU_GROUP_REQUEST);
        List<MenuGroupResponse> list = menuGroupService.list();

        assertThat(list.isEmpty()).isFalse();
    }
}