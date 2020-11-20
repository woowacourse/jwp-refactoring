package kitchenpos.ui;

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static java.util.Collections.singletonList;
import static kitchenpos.fixture.FixtureFactory.createMenu;
import static kitchenpos.fixture.FixtureFactory.createMenuProduct;
import static kitchenpos.ui.MenuRestController.MENU_API;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = MenuRestController.class)
class MenuRestControllerTest extends ControllerTest {
    @MockBean
    private MenuService menuService;

    @DisplayName("메뉴 생성 요청")
    @Test
    void create() throws Exception {
        Menu request = createMenu(null, "후라이드+후라이드", BigDecimal.valueOf(19_000L), 1L,
                singletonList(createMenuProduct(null, 1L, 2L)));
        String body = objectMapper.writeValueAsString(request);

        when(menuService.create(any())).thenReturn(new Menu());

        requestWithPost(MENU_API, body);
    }

    @DisplayName("메뉴 목록 조회 요청")
    @Test
    void list() throws Exception {
        requestWithGet(MENU_API);
    }
}