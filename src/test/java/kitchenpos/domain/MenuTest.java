package kitchenpos.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    private final Product product = new Product.Builder()
            .name("product")
            .price(BigDecimal.valueOf(10000))
            .build();

    private final MenuProduct menuProduct = new MenuProduct.Builder()
            .product(product)
            .quantity(1L)
            .build();

    @DisplayName("메뉴 빌더로 메뉴를 생성할 수 있다.")
    @Test
    void createMenuWithBuilder() {
        final Menu menu = new Menu.Builder()
                .id(1L)
                .name("menu")
                .price(BigDecimal.valueOf(10000))
                .menuGroup(null)
                .menuProducts(Arrays.asList(menuProduct))
                .build();

        assertThat(menu.getId()).isEqualTo(1L);
        assertThat(menu.getName()).isEqualTo("menu");
        assertThat(menu.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(10000));
        assertThat(menu.getMenuGroupId()).isNull();
        assertThat(menu.getMenuProducts()).hasSize(1);
    }

    @DisplayName("메뉴의 가격은 null 이거나 음수일 수 없다.")
    @Test
    void menuPriceMustBePositive() {
        assertThatThrownBy(() -> new Menu.Builder()
                .id(1L)
                .name("menu")
                .price(null)
                .menuGroup(null)
                .menuProducts(Arrays.asList(menuProduct))
                .build()
        ).isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new Menu.Builder()
                .id(1L)
                .name("menu")
                .price(BigDecimal.valueOf(-10000))
                .menuGroup(null)
                .menuProducts(Arrays.asList(menuProduct))
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 구성하는 MenuProduct는 null일 수 없다.")
    @Test
    void menuProductsMustNotBeNull() {
        assertThatThrownBy(() -> new Menu.Builder()
                .id(1L)
                .name("menu")
                .price(BigDecimal.valueOf(10000))
                .menuGroup(null)
                .menuProducts(null)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 구성하는 MenuProduct의 가격의 총합보다 비싸게 Menu의 Price를 책정할 수 없다.")
    @Test
    void menuCannotBeExpensiveThanMenuProductPriceTotal() {
        assertThatThrownBy(() -> new Menu.Builder()
                .id(1L)
                .name("menu")
                .price(BigDecimal.valueOf(999999999))
                .menuGroup(null)
                .menuProducts(Arrays.asList(menuProduct))
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 구성하는 MenuProduct의 가격의 총합보다 같거나 싸게 Menu의 Price를 책정해야 한다.")
    @Test
    void menuShouldBeEqualOrCheaperThanMenuProductPriceTotal() {
        final Menu samePriceMenu = new Menu.Builder()
                .id(1L)
                .name("menu")
                .price(product.getPrice())
                .menuGroup(null)
                .menuProducts(Arrays.asList(menuProduct))
                .build();

        assertThat(samePriceMenu.getPrice()).isEqualByComparingTo(product.getPrice());

        final Menu cheaperPriceMenu = new Menu.Builder()
                .id(1L)
                .name("menu")
                .price(BigDecimal.valueOf(100))
                .menuGroup(null)
                .menuProducts(Arrays.asList(menuProduct))
                .build();

        assertThat(cheaperPriceMenu.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }
}
