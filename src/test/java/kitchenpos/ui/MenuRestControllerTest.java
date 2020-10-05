package kitchenpos.ui;

import kitchenpos.TestObjectFactory;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MenuRestController.class)
class MenuRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @DisplayName("Menu 생성 요청 테스트")
    @Test
    void create() throws Exception {
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(TestObjectFactory.createMenuProduct(1L, 1L, 2));
        Menu menu = TestObjectFactory.createMenuDto("후라이드+후라이드", 19000, 1L, menuProducts);
        menu.setId(1L);

        given(menuService.create(any())).willReturn(menu);

        mockMvc.perform(post("/api/menus")
                .content("{\n"
                        + "  \"name\": \"후라이드+후라이드\",\n"
                        + "  \"price\": 19000,\n"
                        + "  \"menuGroupId\": 1,\n"
                        + "  \"menuProducts\": [\n"
                        + "    {\n"
                        + "      \"productId\": 1,\n"
                        + "      \"quantity\": 2\n"
                        + "    }\n"
                        + "  ]\n"
                        + "}")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/menus/1"))
                .andExpect(jsonPath("$.id", Matchers.instanceOf(Number.class)))
                .andExpect(jsonPath("$.name", Matchers.instanceOf(String.class)))
                .andExpect(jsonPath("$.price", Matchers.instanceOf(Number.class)))
                .andExpect(jsonPath("$.menuGroupId", Matchers.instanceOf(Number.class)))
                .andExpect(jsonPath("$.menuProducts[0].menuId", Matchers.instanceOf(Number.class)))
                .andExpect(jsonPath("$.menuProducts[0].productId", Matchers.instanceOf(Number.class)))
                .andExpect(jsonPath("$.menuProducts[0].quantity", Matchers.instanceOf(Number.class)));
    }

    @DisplayName("메뉴 목록 조회 요청 테스트")
    @Test
    void list() throws Exception {
        List<Menu> menus = new ArrayList<>();
        menus.add(TestObjectFactory.createMenuDto("name", 1, 1, new ArrayList<>()));
        menus.add(TestObjectFactory.createMenuDto("name", 1, 1, new ArrayList<>()));
        given(menuService.list()).willReturn(menus);

        mockMvc.perform(get("/api/menus"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }
}
