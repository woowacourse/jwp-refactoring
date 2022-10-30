package kitchenpos.ui;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.MenuService;
import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.application.dto.request.MenuRequest;
import kitchenpos.application.dto.response.MenuProductResponse;
import kitchenpos.application.dto.response.MenuResponse;

@DisplayName("Menu API 테스트")
class MenuRestControllerTest extends RestControllerTest {

    private static final String MENU_NAME = "후라이드 치킨 세트";
    private static final BigDecimal PRICE = new BigDecimal(15_000);
    private static final Long MENU_GROUP_ID = null;
    private static final List<MenuProductRequest> MENU_PRODUCT_REQUESTS = null;
    private static final List<MenuProductResponse> MENU_PRODUCT_RESPONSES = null;

    @DisplayName("메뉴를 생성한다")
    @Test
    void create() throws Exception {
        final MenuRequest request = new MenuRequest(MENU_NAME, PRICE, MENU_GROUP_ID, MENU_PRODUCT_REQUESTS);
        final String body = objectMapper.writeValueAsString(request);

        final MenuResponse response = new MenuResponse(1L, MENU_NAME, PRICE, MENU_GROUP_ID, MENU_PRODUCT_RESPONSES);
        BDDMockito.given(menuService.create(any()))
                .willReturn(response);

        mockMvc.perform(post("/api/menus")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("name", containsString(response.getName())))
            .andExpect(jsonPath("price", is(response.getPrice().intValue())))
        ;
    }

    @DisplayName("메뉴 목록을 조회한다")
    @Test
    void list() throws Exception {
        final MenuResponse response = new MenuResponse(1L, MENU_NAME, PRICE, MENU_GROUP_ID, MENU_PRODUCT_RESPONSES);
        BDDMockito.given(menuService.list())
                .willReturn(List.of(response));

        mockMvc.perform(get("/api/menus"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
        ;
    }
}
