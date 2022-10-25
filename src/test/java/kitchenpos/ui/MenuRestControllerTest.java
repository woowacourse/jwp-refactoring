package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import kitchenpos.ControllerTest;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest extends ControllerTest {

    private final String defaultMenuUrl = "/api/menus";

    @MockBean
    private MenuService menuService;

    @Test
    void 메뉴를_생성할_수_있다() throws Exception {
        // given
        BigDecimal price = BigDecimal.valueOf(13000);
        Menu menu = new Menu(1L, "pasta", price, 1L, new ArrayList<>());
        when(menuService.create(any(Menu.class))).thenReturn(menu);

        // when
        ResultActions response = postRequestWithJson(defaultMenuUrl, menu);

        // then
        response.andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(menu)));
    }

    @Test
    void 메뉴_목록을_조회할_수_있다() throws Exception {
        // given
        BigDecimal price = BigDecimal.valueOf(13000);
        Menu menu = new Menu(1L, "pasta", price, 1L, new ArrayList<>());
        when(menuService.list()).thenReturn(Arrays.asList(menu));

        // when
        ResultActions response = getRequest(defaultMenuUrl);

        // then
        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(Arrays.asList(menu))));
    }
}
