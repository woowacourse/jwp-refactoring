package kitchenpos.ui;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.application.MenuService;
import kitchenpos.ui.dto.MenuProductResponse;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {

    private static final MenuResponse MENU;

    static {
        final Long menuId = 1L;
        final String menuName = "후라이드+후라이드";
        final BigDecimal menuPrice = BigDecimal.valueOf(19_000);
        final long menuGroupId = 10L;
        final long menuProductSeq = 100L;
        final long menuProductProductId = 1_000L;
        final int menuProductQuantity = 2;

        MenuProductResponse menuProduct = MenuProductResponse
            .of(menuProductSeq, menuId, menuProductProductId, menuProductQuantity);
        MENU = MenuResponse
            .of(menuId, menuName, menuPrice, menuGroupId, Collections.singletonList(menuProduct));
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @DisplayName("메뉴 추가")
    @Test
    void create() throws Exception {
        String requestBody = "{\n"
            + "  \"name\": \"" + MENU.getName() + "\",\n"
            + "  \"price\": " + MENU.getPrice() + ",\n"
            + "  \"menuGroupId\": " + MENU.getMenuGroupId() + ",\n"
            + "  \"menuProducts\": [\n"
            + "    {\n"
            + "      \"productId\": " + MENU.getMenuProducts().get(0).getProductId() + ",\n"
            + "      \"quantity\": " + MENU.getMenuProducts().get(0).getQuantity() + "\n"
            + "    }\n"
            + "  ]\n"
            + "}";

        given(menuService.create(any(MenuRequest.class)))
            .willReturn(MENU);

        ResultActions resultActions = mockMvc.perform(post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andDo(print());

        resultActions
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(jsonPath("$.id", is(MENU.getId().intValue())))
            .andExpect(jsonPath("$.name", is(MENU.getName())))
            .andExpect(jsonPath("$.price", is(MENU.getPrice().intValue())))
            .andExpect(jsonPath("$.menuGroupId", is(MENU.getMenuGroupId().intValue())))
            .andExpect(jsonPath("$.menuProducts", hasSize(1)))
            .andExpect(jsonPath("$.menuProducts[0].seq",
                is(MENU.getMenuProducts().get(0).getSeq().intValue())))
            .andExpect(jsonPath("$.menuProducts[0].menuId",
                is(MENU.getMenuProducts().get(0).getMenuId().intValue())))
            .andExpect(jsonPath("$.menuProducts[0].productId",
                is(MENU.getMenuProducts().get(0).getProductId().intValue())))
            .andExpect(jsonPath("$.menuProducts[0].quantity",
                is((int) MENU.getMenuProducts().get(0).getQuantity())))
            .andDo(print());
    }

    @DisplayName("메뉴 그룹 전체 조회")
    @Test
    void list() throws Exception {
        given(menuService.list())
            .willReturn(Collections.singletonList(MENU));

        ResultActions resultActions = mockMvc.perform(get("/api/menus")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(MENU.getId().intValue())))
            .andDo(print());
    }
}
