package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

class MenuServiceTest extends ServiceTest {

    @Nested
    @DisplayName("create()")
    class create {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 새로운 메뉴를 생성한다.")
        void create() {
            // given
            Product product = createAndSaveProduct("product", new BigDecimal(1000));
            MenuProduct menuProduct = createMenuProduct(product.getId(), 10L);
            MenuGroup menuGroup = createMenuGroup("menuGroup");

            Menu menu = createMenu(
                "menu",
                new BigDecimal(1000),
                menuGroup.getId(),
                menuProduct
            );

            // when
            Menu savedMenu = menuService.create(menu);

            // then
            assertAll(
                () -> assertThat(savedMenu).isNotNull(),
                () -> assertThat(savedMenu.getId()).isNotNull()
            );
        }

        @Test
        @DisplayName("가격이 빈값일 경우 예외가 발생한다.")
        void nullPrice() {
            // given
            Product product = createAndSaveProduct("product", new BigDecimal(1000));
            MenuProduct menuProduct = createMenuProduct(product.getId(), 10L);
            MenuGroup menuGroup = createMenuGroup("menuGroup");

            Menu menu = createMenu(
                "menu",
                null,
                menuGroup.getId(),
                menuProduct
            );

            // when, then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가격이 0 미만일 경우 예외가 발생한다.")
        void negativePrice() {
            // given
            Product product = createAndSaveProduct("product", new BigDecimal(1000));
            MenuProduct menuProduct = createMenuProduct(product.getId(), 10L);
            MenuGroup menuGroup = createMenuGroup("menuGroup");

            Menu menu = createMenu(
                "menu",
                new BigDecimal(-1),
                menuGroup.getId(),
                menuProduct
            );

            // when, then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("존재하지 않는 menu group id인 경우 예외가 발생한다.")
        void invalidMenuGroupId() {
            // given
            Product product = createAndSaveProduct("product", new BigDecimal(1000));
            MenuProduct menuProduct = createMenuProduct(product.getId(), 10L);

            Menu menu = createMenu(
                "menu",
                new BigDecimal(1000),
                0L,
                menuProduct
            );

            // when, then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("존재하지 않는 menu product id인 경우 예외가 발생한다.")
        void invalidMenuProductId() {
            // given
            MenuProduct menuProduct = createMenuProduct(0L, 10L);
            MenuGroup menuGroup = createMenuGroup("menuGroup");

            Menu menu = createMenu(
                "menu",
                new BigDecimal(1000),
                menuGroup.getId(),
                menuProduct
            );

            // when, then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("각 상품의 합보다 가격이 큰 값일 경우 예외가 발생한다.")
        void biggerThanProductPriceSum() {
            // given
            Product product = createAndSaveProduct("product", new BigDecimal(1000));
            MenuProduct menuProduct = createMenuProduct(product.getId(), 1L);
            MenuGroup menuGroup = createMenuGroup("menuGroup");

            Menu menu = createMenu(
                "menu",
                new BigDecimal(2000),
                menuGroup.getId(),
                menuProduct
            );

            // when, then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Test
    void list() {
        List<Menu> menus = menuService.list();
        assertThat(menus).isNotNull();
    }
}
