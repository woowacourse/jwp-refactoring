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
import kitchenpos.domain.MenuProduct;
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
    private static final int MENU_PRICE = 19_000;
    private static final long MENU_GROUP_ID = 10L;
    private static final long MENU_PRODUCT_SEQ = 100L;
    private static final long MENU_PRODUCT_PRODUCT_ID = 1_000L;
    private static final int MENU_PRODUCT_QUANTITY = 2;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @DisplayName("메뉴 추가")
    @Test
    void create() throws Exception {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(MENU_PRODUCT_SEQ);
        menuProduct.setMenuId(MENU_ID);
        menuProduct.setProductId(MENU_PRODUCT_PRODUCT_ID);
        menuProduct.setQuantity(MENU_PRODUCT_QUANTITY);

        Menu menu = new Menu();
        menu.setId(MENU_ID);
        menu.setName(MENU_NAME);
        menu.setPrice(BigDecimal.valueOf(MENU_PRICE));
        menu.setMenuGroupId(MENU_GROUP_ID);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        String body = "{\n"
            + "  \"name\": \"" + menu.getName() + "\",\n"
            + "  \"price\": " + menu.getPrice() + ",\n"
            + "  \"menuGroupId\": " + menu.getMenuGroupId() + ",\n"
            + "  \"menuProducts\": [\n"
            + "    {\n"
            + "      \"productId\": " + menuProduct.getProductId() + ",\n"
            + "      \"quantity\": " + menuProduct.getQuantity() + "\n"
            + "    }\n"
            + "  ]\n"
            + "}";

        given(menuService.create(any()))
            .willReturn(menu);

        ResultActions resultActions = mockMvc.perform(post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print());

        resultActions
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(jsonPath("$.id", is(menu.getId().intValue())))
            .andExpect(jsonPath("$.name", is(menu.getName())))
            .andExpect(jsonPath("$.price", is(menu.getPrice().intValue())))
            .andExpect(jsonPath("$.menuGroupId", is(menu.getMenuGroupId().intValue())))
            .andExpect(jsonPath("$.menuProducts", hasSize(1)))
            .andExpect(jsonPath("$.menuProducts[0].seq", is(menuProduct.getSeq().intValue())))
            .andExpect(jsonPath("$.menuProducts[0].menuId", is(menuProduct.getMenuId().intValue())))
            .andExpect(jsonPath("$.menuProducts[0].productId",
                is(menuProduct.getProductId().intValue())))
            .andExpect(jsonPath("$.menuProducts[0].quantity", is((int) menuProduct.getQuantity())))
            .andDo(print());
    }

    @DisplayName("메뉴 그룹 전체 조회")
    @Test
    void list() throws Exception {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(MENU_PRODUCT_SEQ);
        menuProduct.setMenuId(MENU_ID);
        menuProduct.setProductId(MENU_PRODUCT_PRODUCT_ID);
        menuProduct.setQuantity(MENU_PRODUCT_QUANTITY);

        Menu menu = new Menu();
        menu.setId(MENU_ID);
        menu.setName(MENU_NAME);
        menu.setPrice(BigDecimal.valueOf(MENU_PRICE));
        menu.setMenuGroupId(MENU_GROUP_ID);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        given(menuService.list())
            .willReturn(Collections.singletonList(menu));

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
