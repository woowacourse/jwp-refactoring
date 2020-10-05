package kitchenpos.service;

import static kitchenpos.utils.TestObjectUtils.createMenu;
import static kitchenpos.utils.TestObjectUtils.createMenuProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴 생성 - 성공")
    @Test
    void create_SuccessToCreate() {
        String menuName = "후라이드+후라이드";
        BigDecimal menuPrice = BigDecimal.valueOf(19000L);
        Long menuGroupId = 1L;
        List<MenuProduct> menuProducts = Collections.singletonList(createMenuProduct(1L, 2L));
        Menu menu = createMenu(menuName, menuPrice, menuGroupId, menuProducts);

        Menu savedMenu = menuService.create(menu);

        assertAll(() -> {
            assertThat(savedMenu.getId()).isNotNull();
            assertThat(savedMenu.getName()).isEqualTo(menuName);
            assertThat(savedMenu.getPrice()).isEqualByComparingTo(menuPrice);
            assertThat(savedMenu.getMenuGroupId()).isEqualTo(menuGroupId);
            assertThat(savedMenu.getMenuProducts()).hasSize(menuProducts.size());
        });
    }

    @DisplayName("메뉴 생성 - 예외, 메뉴 가격이 null인 경우")
    @Test
    void create_IfMenuPriceIsNull_ThrownException() {
        String menuName = "후라이드+후라이드";
        BigDecimal menuPrice = null;
        Long menuGroupId = 1L;
        List<MenuProduct> menuProducts = Collections.singletonList(createMenuProduct(1L, 2L));
        Menu menu = createMenu(menuName, menuPrice, menuGroupId, menuProducts);

        assertThatThrownBy(() -> {
            menuService.create(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 - 예외, 메뉴 가격이 음수인 경우")
    @Test
    void create_IfMenuPriceIsNegative_ThrownException() {
        String menuName = "후라이드+후라이드";
        BigDecimal menuPrice = BigDecimal.valueOf(-10000L);
        Long menuGroupId = 1L;
        List<MenuProduct> menuProducts = Collections.singletonList(createMenuProduct(1L, 2L));
        Menu menu = createMenu(menuName, menuPrice, menuGroupId, menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 - 예외, 메뉴 그룹이 존재하지 않는 경우")
    @Test
    void create_IfMenuGroupIsNotExist_ThrownException() {
        String menuName = "후라이드+후라이드";
        BigDecimal menuPrice = BigDecimal.valueOf(19000L);
        Long menuGroupId = Long.MAX_VALUE;
        List<MenuProduct> menuProducts = Collections.singletonList(createMenuProduct(1L, 2L));
        Menu menu = createMenu(menuName, menuPrice, menuGroupId, menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 - 예외, 메뉴 가격이 메뉴 상품의 총 합보다 큰 경우")
    @Test
    void create_IfMenuPriceIsGreaterThanAllMenuProduct_ThrownException() {
        String menuName = "후라이드+후라이드";
        BigDecimal menuPrice = BigDecimal.valueOf(Long.MAX_VALUE);
        Long menuGroupId = Long.MAX_VALUE;
        List<MenuProduct> menuProducts = Collections.singletonList(createMenuProduct(1L, 2L));
        Menu menu = createMenu(menuName, menuPrice, menuGroupId, menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 전체 조회 - 성공")
    @Test
    void list_SuccessToFindAll() {
        List<Menu> menus = menuService.list();

        assertThat(menus).isNotEmpty();
    }
}
