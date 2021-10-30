package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.KitchenPosTestFixture;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ContextConfiguration(classes = MenuRestController.class)
@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest extends KitchenPosTestFixture {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    @Test
    void create() throws Exception {
        // given
        Menu menu = 메뉴를_저장한다(1L, "닭강정", BigDecimal.valueOf(1000), 1L,
                Collections.singletonList(메뉴_상품을_저장한다(1L, 1L, 1L, 100L)));

        // when
        given(menuService.create(ArgumentMatchers.any())).willReturn(menu);

        // then
        mvc.perform(post("/api/menus")
                        .content(objectMapper.writeValueAsString(menu))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("닭강정")))
                .andExpect(jsonPath("$.price", is(1000)))
                .andExpect(jsonPath("$.menuGroupId", is(1)))
                .andExpect(jsonPath("$.menuProducts", hasSize(1)));
    }

    @Test
    void list() throws Exception {
        // given
        MenuProduct product1 = 메뉴_상품을_저장한다(1L, 1L, 1L, 100L);
        MenuProduct product2 = 메뉴_상품을_저장한다(2L, 2L, 2L, 200L);

        List<Menu> menus = Arrays.asList(
                메뉴를_저장한다(1L, "menu1", BigDecimal.valueOf(1000), 1L, Collections.singletonList(product1)),
                메뉴를_저장한다(2L, "menu2", BigDecimal.valueOf(3000), 1L, Collections.singletonList(product2))
        );

        // when
        given(menuService.list()).willReturn(menus);

        // then
        mvc.perform(MockMvcRequestBuilders.get("/api/menus")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("menu1")))
                .andExpect(jsonPath("$[0].price", is(1000)))
                .andExpect(jsonPath("$[0].menuGroupId", is(1)))
                .andExpect(jsonPath("$[0].menuProducts", hasSize(1)))
                .andExpect(jsonPath("$[0].menuProducts[0].seq", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("menu2")))
                .andExpect(jsonPath("$[1].price", is(3000)))
                .andExpect(jsonPath("$[1].menuGroupId", is(1)))
                .andExpect(jsonPath("$[1].menuProducts", hasSize(1)))
                .andExpect(jsonPath("$[1].menuProducts[0].seq", is(2)));
    }
}