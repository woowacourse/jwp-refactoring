package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        MenuProduct menuProduct = getMenuProduct(2, 1L);
        Menu menu = getMenu("후라이드두마리", 32000, 1L, Arrays.asList(menuProduct));

        Menu persist = menuService.create(menu);

        assertAll(
            () -> assertThat(persist).isEqualToIgnoringGivenFields(menu, "id", "menuProducts"),
            () -> assertThat(persist.getMenuProducts().get(0)).isEqualToIgnoringNullFields(menuProduct)
        );
    }

    @DisplayName("메뉴의 가격이 올바르지 않으면 메뉴를 등록할 수 없다.")
    @Test
    void create_willThrowException_whenPriceIsNull() {
        MenuProduct menuProduct = getMenuProduct(2, 1L);
        Menu menu = getMenuWithNullPrice("후라이드두마리", 1L, Arrays.asList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 음수면 메뉴를 등록할 수 없다.")
    @Test
    void create_willThrowException_whenPriceIsNegativeNumber() {
        MenuProduct menuProduct = getMenuProduct(2, 1L);
        Menu menu = getMenu("후라이드두마리", -1000, 1L, Arrays.asList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가 특정 메뉴 그룹에 속하지 않으면 메뉴를 등록할 수 없다.")
    @Test
    void create_willThrowException_whenMenuGroupIsNotSpecific() {
        MenuProduct menuProduct = getMenuProduct(2, 1L);
        Menu menu = getMenu("후라이드두마리", 32000, 5L, Arrays.asList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 속한 상품이 존재하지 않으면 메뉴를 등록할 수 없다.")
    @Test
    void create_willThrowException_whenProductDoesNotExist() {
        MenuProduct menuProduct = getMenuProduct(2, 7L);
        Menu menu = getMenu("후라이드두마리", 32000, 1L, Arrays.asList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴에 속한 상품 금액의 합보다 크면 메뉴를 등록할 수 없다.")
    @Test
    void create_willThrowException_whenMenuPriceIsOverProductPrices() {
        MenuProduct friedChicken = getMenuProduct(2, 1L);
        MenuProduct seasonedChicken = getMenuProduct(2, 1L);
        Menu menu = getMenu("후라이드두마리", 65000, 5L, Arrays.asList(friedChicken, seasonedChicken));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
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