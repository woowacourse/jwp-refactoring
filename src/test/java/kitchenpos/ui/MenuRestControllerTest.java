package kitchenpos.ui;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.ui.factory.MenuBuilder;
import kitchenpos.ui.factory.MenuProductBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest extends BaseWebMvcTest {

    Menu menu1;
    Menu menu2;

    @BeforeEach
    void setUp() {
        MenuProduct menuProduct1 = new MenuProductBuilder()
                .seq(1L)
                .menuId(1L)
                .productId(1L)
                .quantity(2L)
                .build();

        MenuProduct menuProduct2 = new MenuProductBuilder()
                .seq(2L)
                .menuId(2L)
                .productId(1L)
                .quantity(1L)
                .build();

        MenuProduct menuProduct3 = new MenuProductBuilder()
                .seq(3L)
                .menuId(2L)
                .productId(2L)
                .quantity(1L)
                .build();

        menu1 = new MenuBuilder()
                .id(1L)
                .name("후라이드 두마리")
                .price(new BigDecimal(19000))
                .menuGroupId(1L)
                .menuProducts(Arrays.asList(menuProduct1))
                .build();

        menu2 = new MenuBuilder()
                .id(2L)
                .name("후라이드 한마리 + 양념 한마리")
                .price(new BigDecimal(20000))
                .menuGroupId(2L)
                .menuProducts(Arrays.asList(menuProduct2, menuProduct3))
                .build();
    }

    @DisplayName("POST /api/menus -> 메뉴를 추가한다.")
    @Test
    void create() throws Exception {
        // given
        given(menuService.create(any(Menu.class)))
                .willReturn(menu1);

        MenuProduct requestMenuProduct = new MenuProductBuilder()
                .seq(null)
                .menuId(null)
                .productId(1L)
                .quantity(2L)
                .build();
        Menu requestMenu = new MenuBuilder()
                .id(null)
                .name("후라이드 두마리")
                .price(new BigDecimal(19000))
                .menuGroupId(1L)
                .menuProducts(Arrays.asList(requestMenuProduct))
                .build();

        String content = parseJson(requestMenu);

        // when
        ResultActions actions = mvc.perform(super.postRequest("/api/menus", content));

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/menus/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("후라이드 두마리")))
                .andExpect(jsonPath("$.price", is(19000)))
                .andExpect(jsonPath("$.menuGroupId", is(1)))
                .andExpect(jsonPath("$.menuProducts[0].seq", is(1)))
                .andExpect(jsonPath("$.menuProducts[0].menuId", is(1)))
                .andExpect(jsonPath("$.menuProducts[0].productId", is(1)))
                .andExpect(jsonPath("$.menuProducts[0].quantity", is(2)))
                .andDo(print());
    }

    @DisplayName("GET /api/menus -> 메뉴 전체를 조회한다.")
    @Test
    void list() throws Exception {
        // given
        given(menuService.list())
                .willReturn(Arrays.asList(menu1, menu2));

        // when
        ResultActions actions = mvc.perform(super.getRequest("/api/menus"));

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("후라이드 두마리")))
                .andExpect(jsonPath("$[0].price", is(19000)))
                .andExpect(jsonPath("$[0].menuGroupId", is(1)))
                .andExpect(jsonPath("$[0].menuProducts[0].seq", is(1)))
                .andExpect(jsonPath("$[0].menuProducts[0].menuId", is(1)))
                .andExpect(jsonPath("$[0].menuProducts[0].productId", is(1)))
                .andExpect(jsonPath("$[0].menuProducts[0].quantity", is(2)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("후라이드 한마리 + 양념 한마리")))
                .andExpect(jsonPath("$[1].price", is(20000)))
                .andExpect(jsonPath("$[1].menuGroupId", is(2)))
                .andExpect(jsonPath("$[1].menuProducts[0].seq", is(2)))
                .andExpect(jsonPath("$[1].menuProducts[0].menuId", is(2)))
                .andExpect(jsonPath("$[1].menuProducts[0].productId", is(1)))
                .andExpect(jsonPath("$[1].menuProducts[0].quantity", is(1)))
                .andExpect(jsonPath("$[1].menuProducts[1].seq", is(3)))
                .andExpect(jsonPath("$[1].menuProducts[1].menuId", is(2)))
                .andExpect(jsonPath("$[1].menuProducts[1].productId", is(2)))
                .andExpect(jsonPath("$[1].menuProducts[1].quantity", is(1)))
                .andDo(print());
    }
}