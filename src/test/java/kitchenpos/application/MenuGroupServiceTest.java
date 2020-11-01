package kitchenpos.application;

import static kitchenpos.fixture.RequestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.application.dto.MenuGroupResponse;

@SpringBootTest
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void create() {
        Long menuId = menuGroupService.create(MENU_GROUP_REQUEST);

        assertThat(menuId).isNotNull();
    }

    @Test
    void list() {
        menuGroupService.create(MENU_GROUP_REQUEST);
        List<MenuGroupResponse> list = menuGroupService.list();

        assertThat(list.isEmpty()).isFalse();
    }
}