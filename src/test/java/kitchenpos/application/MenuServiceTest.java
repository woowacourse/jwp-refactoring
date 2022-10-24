package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@Rollback
@SpringBootTest
public class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @DisplayName("menu 가격이 null이거나 0보다 작은 경우 예외가 발생한다.")
    @Test
    void create_ifMenuPriceIsNullOrMinus_throwsException() {
        // given
        final MenuProduct menuProduct1 = new MenuProduct(1L, 3);
        final MenuProduct menuProduct2 = new MenuProduct(2L, 3);
        final List<MenuProduct> menuProducts = Arrays.asList(menuProduct1, menuProduct2);
        final Menu menu = new Menu("메뉴1", BigDecimal.valueOf(-1000), 1L, menuProducts);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("menu의 menuGroup이 존재하지 않은 경우 예외가 발생한다.")
    @Test
    void create_ifMenuGroupNotExist_throwsException() {
        // given
        final MenuProduct menuProduct1 = new MenuProduct(1L, 3);
        final MenuProduct menuProduct2 = new MenuProduct(2L, 3);
        final List<MenuProduct> menuProducts = Arrays.asList(menuProduct1, menuProduct2);
        final Menu menu = new Menu("메뉴1", BigDecimal.valueOf(1000), 5L, menuProducts);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("menu의 menuProduct가 존재하지 않은 경우 예외가 발생한다.")
    @Test
    void create_ifMenuProductNotExist_throwsExcpetion() {
        // given
        final MenuProduct menuProduct1 = new MenuProduct(7L, 3);
        final MenuProduct menuProduct2 = new MenuProduct(2L, 3);
        final List<MenuProduct> menuProducts = Arrays.asList(menuProduct1, menuProduct2);
        final Menu menu = new Menu("메뉴1", BigDecimal.valueOf(1000), 5L, menuProducts);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("menu의 가격이 menu의 product들의 가격보다 비싼 경우 예외가 발생한다.")
    @Test
    void create_ifMenuPriceMoreExpensiveThanProducts_throwsException() {
        // given
        final MenuProduct menuProduct1 = new MenuProduct(1L, 1);
        final MenuProduct menuProduct2 = new MenuProduct(2L, 1);
        final List<MenuProduct> menuProducts = Arrays.asList(menuProduct1, menuProduct2);
        final Menu menu = new Menu("메뉴1", BigDecimal.valueOf(40000), 1L, menuProducts);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("menu가 정상적으로 저장이 된다.")
    @Test
    void create() {
        // given
        final MenuProduct menuProduct1 = new MenuProduct(1L, 1);
        final MenuProduct menuProduct2 = new MenuProduct(2L, 1);
        final List<MenuProduct> menuProducts = Arrays.asList(menuProduct1, menuProduct2);
        final Menu menu = new Menu("메뉴1", BigDecimal.valueOf(32000), 1L, menuProducts);

        // when
        final Menu savedMenu = menuService.create(menu);

        // then
        assertThat(savedMenu.getId()).isNotNull();
    }

    @DisplayName("menu들을 조회한다.")
    @Test
    void list() {
        // when
        final List<Menu> menus = menuService.list();

        final List<Long> ids = menus.stream()
                .map(Menu::getId)
                .collect(Collectors.toList());

        // then
        assertThat(ids).containsExactly(1L, 2L, 3L, 4L, 5L, 6L);
    }
}
