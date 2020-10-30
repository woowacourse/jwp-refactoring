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
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.MenuProductRequest;
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

    private static final Long MENU_ID = 1L;
    private static final String MENU_NAME = "후라이드+후라이드";
    private static final BigDecimal MENU_PRICE = BigDecimal.valueOf(19_000);
    private static final long MENU_GROUP_ID = 10L;
    private static final MenuGroup MENU_GROUP = MenuGroup.of(MENU_GROUP_ID, "메뉴 그룹");
    private static final long MENU_PRODUCT_SEQ = 100L;
    private static final long MENU_PRODUCT_PRODUCT_ID = 1_000L;
    private static final int MENU_PRODUCT_QUANTITY = 2;

    private static final MenuProduct MENU_PRODUCT = MenuProduct.of(MENU_PRODUCT_SEQ, null,
        Product.of(MENU_PRODUCT_PRODUCT_ID, "PRODUCTNAME", BigDecimal.ONE),
        MENU_PRODUCT_QUANTITY);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @DisplayName("메뉴 추가")
    @Test
    void create() throws Exception {
        MenuProductRequest menuProductRequest = new MenuProductRequest(MENU_PRODUCT_PRODUCT_ID,
            MENU_PRODUCT_QUANTITY);

        MenuRequest menuRequest = new MenuRequest(MENU_NAME, MENU_PRICE, MENU_GROUP_ID,
            Collections.singletonList(menuProductRequest));

        String requestBody = "{\n"
            + "  \"name\": \"" + menuRequest.getName() + "\",\n"
            + "  \"price\": " + menuRequest.getPrice() + ",\n"
            + "  \"menuGroupId\": " + menuRequest.getMenuGroupId() + ",\n"
            + "  \"menuProducts\": [\n"
            + "    {\n"
            + "      \"productId\": " + menuProductRequest.getProductId() + ",\n"
            + "      \"quantity\": " + menuProductRequest.getQuantity() + "\n"
            + "    }\n"
            + "  ]\n"
            + "}";

        Menu menu = Menu.of(MENU_ID, MENU_NAME, MENU_PRICE, MENU_GROUP,
            Collections.singletonList(MENU_PRODUCT));

        given(menuService.create(any(MenuRequest.class)))
            .willReturn(MenuResponse.of(menu));

        ResultActions resultActions = mockMvc.perform(post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andDo(print());

        MenuResponse menuResponse = MenuResponse.of(menu);

        resultActions
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(jsonPath("$.id", is(menuResponse.getId().intValue())))
            .andExpect(jsonPath("$.name", is(menuResponse.getName())))
            .andExpect(jsonPath("$.price", is(menuResponse.getPrice().intValue())))
            .andExpect(jsonPath("$.menuGroupId", is(menuResponse.getMenuGroupId().intValue())))
            .andExpect(jsonPath("$.menuProducts", hasSize(1)))
            .andExpect(
                jsonPath("$.menuProducts[0].seq", is(MENU_PRODUCT.getSeq().intValue())))
            .andExpect(
                jsonPath("$.menuProducts[0].menuId", is(MENU_PRODUCT.getMenu().getId().intValue())))
            .andExpect(jsonPath("$.menuProducts[0].productId",
                is(menuProductRequest.getProductId().intValue())))
            .andExpect(
                jsonPath("$.menuProducts[0].quantity", is((int) menuProductRequest.getQuantity())))
            .andDo(print());
    }

    @DisplayName("메뉴 그룹 전체 조회")
    @Test
    void list() throws Exception {
        Menu menu = Menu.of(MENU_ID, MENU_NAME, MENU_PRICE, MENU_GROUP,
            Collections.singletonList(MENU_PRODUCT));

        given(menuService.list())
            .willReturn(MenuResponse.listOf(Collections.singletonList(menu)));

        ResultActions resultActions = mockMvc.perform(get("/api/menus")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(menu.getId().intValue())))
            .andDo(print());
    }
}
