package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴 생성")
    @Test
    @Transactional
    void create() {
        long menuId = 7L;
        String name = "터틀치킨";
        BigDecimal price = BigDecimal.valueOf(16_000L);
        long menuGroupId = 1L;

        List<MenuProduct> menuProducts = Collections.singletonList(createMenuProduct(menuId, 1L, 1L));
        Menu menu = createMenu(menuId, name, price, menuGroupId, menuProducts);

        Menu savedMenu = menuService.create(menu);

        assertThat(savedMenu.getId()).isEqualTo(menuId);
        assertThat(savedMenu.getName()).isEqualTo(name);
        assertThat(savedMenu.getPrice().longValue()).isEqualTo(price.longValue());
        assertThat(savedMenu.getMenuGroupId()).isEqualTo(menuGroupId);
    }

    @DisplayName("메뉴의 가격이 올바르지 않은 경우 예외 발생")
    @Test
    void create_throw1() {
        long menuId = 7L;
        String name = "터틀치킨";
        BigDecimal invalidPrice = BigDecimal.valueOf(-1L);
        long menuGroupId = 1L;

        List<MenuProduct> menuProducts = Collections.singletonList(createMenuProduct(menuId, 1L, 1L));
        Menu menu = createMenu(menuId, name, invalidPrice, menuGroupId, menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹이 존재하지 않은 경우 예외 발생")
    @Test
    void create_throw2() {
        long menuId = 7L;
        String name = "터틀치킨";
        BigDecimal price = BigDecimal.valueOf(16_000L);
        long invalidMenuGroupId = 0L;

        List<MenuProduct> menuProducts = Collections.singletonList(createMenuProduct(menuId, 1L, 1L));
        Menu menu = createMenu(menuId, name, price, invalidMenuGroupId, menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    void list() {
        List<Menu> menus = menuService.list();
        assertThat(menus.size()).isEqualTo(6);
    }

    private Menu createMenu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    private MenuProduct createMenuProduct(Long menuId, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}