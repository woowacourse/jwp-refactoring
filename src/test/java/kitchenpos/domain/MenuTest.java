package kitchenpos.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @Test
    @DisplayName("정상적인 Menu 객체 생성 테스트")
    void createMenu() {
        //given
        final String menuName = "Sample Menu";
        final BigDecimal menuPrice = BigDecimal.valueOf(17000);
        final MenuGroup menuGroup = MenuGroup.from("Sample Menu Group");
        final Product product1 = Product.of("Product 1", BigDecimal.valueOf(5000));
        final Product product2 = Product.of("Product 2", BigDecimal.valueOf(7000));
        final Map<Product, Integer> productWithQuantity = new HashMap<>();
        productWithQuantity.put(product1, 2);
        productWithQuantity.put(product2, 1);

        //when
        final Menu menu = Menu.of(menuName, menuPrice, menuGroup, productWithQuantity);

        //then
        assertThat(menu).isNotNull();
        assertThat(menu.getName()).isEqualTo(menuName);
        assertThat(menu.getPrice()).isEqualByComparingTo(menuPrice);
        assertThat(menu.getMenuGroup()).isEqualTo(menuGroup);
        assertThat(menu.getMenuProducts().getMenuProducts()).hasSize(2);
    }

    @Test
    @DisplayName("Menu 생성 시 가격이 음수일 경우 예외 발생")
    void createMenuWithNegativePrice_ShouldThrowException() {
        //given
        final String menuName = "Sample Menu";
        final BigDecimal negativePrice = BigDecimal.valueOf(-5000);
        final MenuGroup menuGroup = MenuGroup.from("Sample Menu Group");
        final Product product1 = Product.of("Product 1", BigDecimal.valueOf(5000));
        final Map<Product, Integer> productWithQuantity = Collections.singletonMap(product1, 1);

        //when
        //then
        assertThatThrownBy(() -> Menu.of(menuName, negativePrice, menuGroup, productWithQuantity))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Menu 생성 시 가격이 null일 경우 예외 발생")
    void createMenuWithNullPrice_ShouldThrowException() {
        //given
        final String menuName = "Sample Menu";
        final BigDecimal nullPrice = null;
        final MenuGroup menuGroup = MenuGroup.from( "Sample Menu Group");
        final Product product1 = Product.of("Product 1", BigDecimal.valueOf(5000));
        final Map<Product, Integer> productWithQuantity = Collections.singletonMap(product1, 1);

        //when
        //then
        assertThatThrownBy(() -> Menu.of(menuName, nullPrice, menuGroup, productWithQuantity))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Menu 생성 시 가격이 제품과 수량 맵의 총 가격보다 큰 경우 예외 발생")
    void createMenuWithPriceGreaterThanTotalProductPrice_ShouldThrowException() {
        //given
        final String menuName = "Sample Menu";
        final BigDecimal menuPrice = BigDecimal.valueOf(20000);
        final MenuGroup menuGroup = MenuGroup.from("Sample Menu Group");
        final Product product1 = Product.of("Product 1", BigDecimal.valueOf(5000));
        final Map<Product, Integer> productWithQuantity = Collections.singletonMap(product1, 2);

        //when
        //then
        assertThatThrownBy(() -> Menu.of(menuName, menuPrice, menuGroup, productWithQuantity))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Menu 생성 시 이름이 null 또는 빈 문자열인 경우 예외 발생")
    void createMenuWithNullOrBlankName_ShouldThrowException() {
        //given
        final BigDecimal menuPrice = BigDecimal.valueOf(15000);
        final MenuGroup menuGroup = MenuGroup.from("Sample Menu Group");
        final Product product1 = Product.of("Product 1", BigDecimal.valueOf(5000));
        final Map<Product, Integer> productWithQuantity = Collections.singletonMap(product1, 1);

        //when
        //then
        assertThatThrownBy(() -> Menu.of(null, menuPrice, menuGroup, productWithQuantity))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> Menu.of("", menuPrice, menuGroup, productWithQuantity))
                .isInstanceOf(IllegalArgumentException.class);
    }
}