package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.menu.dto.response.MenuResponse;

@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Test
    @DisplayName("전체 메뉴를 조회한다.")
    void list() {
        List<MenuResponse> menus = menuService.list();
        assertThat(menus).isNotNull();
    }

}
