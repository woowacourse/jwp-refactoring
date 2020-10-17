package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

class MenuServiceIntegrationTest extends ServiceIntegrationTest {
    @Autowired
    MenuService menuService;

    @DisplayName("메뉴를 추가한다.")
    @Test
    void create() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(7L);
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);
        Menu twoFriedChicken = new Menu();
        twoFriedChicken.setName("후라이드두마리");
        twoFriedChicken.setPrice(BigDecimal.valueOf(32000, 2));
        twoFriedChicken.setMenuGroupId(1L);
        twoFriedChicken.setMenuProducts(Arrays.asList(menuProduct));

        Menu persist = menuService.create(twoFriedChicken);

        assertAll(
            () -> assertThat(persist).isEqualToIgnoringGivenFields(twoFriedChicken, "id", "menuProducts"),
            () -> assertThat(persist.getMenuProducts().get(0)).isEqualToIgnoringNullFields(menuProduct)
        );
    }

    @DisplayName("메뉴 전체를 조회한다.")
    @Test
    void list() {
        List<Menu> menus = menuService.list();

        assertAll(
            () -> assertThat(menus).hasSize(6),
            () -> assertThat(menus.get(0).getName()).isEqualTo("후라이드치킨"),
            () -> assertThat(menus.get(1).getName()).isEqualTo("양념치킨"),
            () -> assertThat(menus.get(2).getName()).isEqualTo("반반치킨"),
            () -> assertThat(menus.get(3).getName()).isEqualTo("통구이"),
            () -> assertThat(menus.get(4).getName()).isEqualTo("간장치킨"),
            () -> assertThat(menus.get(5).getName()).isEqualTo("순살치킨")
        );
    }
}