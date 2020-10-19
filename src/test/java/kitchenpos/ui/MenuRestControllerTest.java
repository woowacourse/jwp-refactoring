package kitchenpos.ui;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

class MenuRestControllerTest extends ControllerTest {
    private static final String MENU_API_URL = "/api/menus";

    @DisplayName("create: 이름을 body message에 포함해 메뉴 등록을 요청시 ,메뉴 생성 성공 시 201 응답을 반환한다.")
    @Test
    void create() throws Exception {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(5L);

        final Menu menu = new Menu();
        menu.setName("후라이드 5마리 세트");
        menu.setPrice(BigDecimal.valueOf(40_000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        final ResultActions resultActions = create(MENU_API_URL, menu);

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("후라이드 5마리 세트")))
                .andExpect(jsonPath("$.price", is(40_000d)))
                .andExpect(jsonPath("$.menuGroupId", is(1)))
                .andExpect(jsonPath("$.menuProducts", hasSize(1)));
    }

    @DisplayName("list: 전체 메뉴 목록 요청시, 200 응답 코드와 함께 메뉴 목록을 반환한다.")
    @Test
    void list() throws Exception {
        final ResultActions resultActions = findList(MENU_API_URL);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)));
    }
}