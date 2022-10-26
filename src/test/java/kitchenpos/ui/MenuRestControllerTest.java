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
        MenuResponse menuResponse = new MenuResponse(1L, "pasta", price, 1L, new ArrayList<>());
        when(menuService.create(any(MenuCreateRequest.class))).thenReturn(menuResponse);

        // when
        ResultActions response = postRequestWithJson(defaultMenuUrl, menuResponse);

        // then
        response.andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(menuResponse)));
    }

    @Test
    void 메뉴_목록을_조회할_수_있다() throws Exception {
        // given
        BigDecimal price = BigDecimal.valueOf(13000);
        MenuResponse menuResponse = new MenuResponse(1L, "pasta", price, 1L, new ArrayList<>());
        List<MenuResponse> menuResponses = Arrays.asList(menuResponse);
        when(menuService.list()).thenReturn(menuResponses);

        // when
        ResultActions response = getRequest(defaultMenuUrl);

        // then
        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(menuResponses)));
    }
}
