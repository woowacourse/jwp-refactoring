package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.ControllerTest;
import kitchenpos.application.MenuService;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.response.MenuResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest extends ControllerTest {

    private static final String MENU_URL = "/api/menus";

    private final MenuResponse menuResponse = new MenuResponse(1L, "pasta", BigDecimal.valueOf(13000), 1L,
            new ArrayList<>());

    @Autowired
    private MenuService menuService;

    @Test
    void 메뉴를_생성할_수_있다() throws Exception {
        // given
        when(menuService.create(any(MenuCreateRequest.class))).thenReturn(menuResponse);

        // when
        ResultActions response = postRequestWithJson(MENU_URL, new MenuCreateRequest());

        // then
        response.andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(menuResponse)));
    }

    @Test
    void 메뉴_목록을_조회할_수_있다() throws Exception {
        // given
        List<MenuResponse> menuResponses = Arrays.asList(menuResponse);
        when(menuService.list()).thenReturn(menuResponses);

        // when
        ResultActions response = getRequest(MENU_URL);

        // then
        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(menuResponses)));
    }
}
