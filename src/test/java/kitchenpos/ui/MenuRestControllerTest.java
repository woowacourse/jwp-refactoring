package kitchenpos.ui;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

class MenuRestControllerTest extends ControllerTest {

    @DisplayName("create: 메뉴 등록 테스트")
    @Test
    void createTest() throws Exception {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2L);
        final Menu menu = new Menu();
        menu.setName("후라이드");
        menu.setPrice(BigDecimal.valueOf(16000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        create("/api/menus", menu)
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value(menu.getName()));
    }

    @DisplayName("list: 전체 메뉴 조회 테스트")
    @Test
    void listTest() throws Exception {
        findList("/api/menus")
                .andExpect(jsonPath("$[0].name").value("후라이드치킨"))
                .andExpect(jsonPath("$[1].name").value("양념치킨"));
    }
}
