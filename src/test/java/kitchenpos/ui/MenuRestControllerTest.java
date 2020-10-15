package kitchenpos.ui;

import kitchenpos.application.MenuService;
import kitchenpos.application.common.TestObjectFactory;
import kitchenpos.dto.menu.MenuProductDto;
import kitchenpos.dto.menu.MenuResponse;
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
        List<MenuProductDto> menuProducts = new ArrayList<>();
        menuProducts.add(TestObjectFactory.createMenuProductDto(1L, 1L, 1L, 2));
        MenuResponse menu = TestObjectFactory.createMenuResponse(1L, "후라이드+후라이드", 19000, 1L, menuProducts);

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
        List<MenuResponse> menus = new ArrayList<>();
        menus.add(TestObjectFactory.createMenuResponse(1L, "양념+양념", 2000, 1L, new ArrayList<>()));
        menus.add(TestObjectFactory.createMenuResponse(2L, "양념+후라이드", 1500, 2L, new ArrayList<>()));
        given(menuService.list()).willReturn(menus);

        mockMvc.perform(get("/api/menus"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }
}
