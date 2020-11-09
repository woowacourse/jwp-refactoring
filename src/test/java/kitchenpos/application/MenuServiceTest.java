package kitchenpos.application;

import static kitchenpos.fixture.RequestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.MenuResponse;

@Transactional
@SpringBootTest
class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴 생성")
    @Test
    void create() {
        Long menuId = menuService.create(MENU_REQUEST).getId();

        assertThat(menuId).isNotNull();
    }

    @DisplayName("메뉴 전체 조회")
    @Test
    void list() {
        menuService.create(MENU_REQUEST);
        List<MenuResponse> list = menuService.list();

        assertThat(list.isEmpty()).isFalse();
    }
}