package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuTest {

    @DisplayName("정상적인 경우 메뉴를 생성할 수 있다.")
    @Test
    void createMenu() {
        final Product product = new Product("후라이드", BigDecimal.valueOf(16000));
        final MenuProduct menuProduct = new MenuProduct(product, 5L);
        final Product product2 = new Product("후라이드", BigDecimal.valueOf(16000));
        final MenuProduct menuProduct2 = new MenuProduct(product2, 5L);
        final MenuGroup menuGroup = new MenuGroup("신메뉴");
        final List<MenuProduct> menuProducts = List.of(menuProduct, menuProduct2);

        final Menu sut = new Menu("두마리메뉴", BigDecimal.valueOf(16000), menuGroup, menuProducts);

        assertAll(
                () -> assertThat(sut.getMenuProducts()).hasSize(2),
                () -> assertThat(sut.getMenuProducts())
                        .usingRecursiveFieldByFieldElementComparator()
                        .usingElementComparatorIgnoringFields("id")
                        .isEqualTo(menuProducts)
        );
    }

    @DisplayName("메뉴 가격이 없는 경우 생성할 수 없다.")
    @Test
    void createMenuWithNullPrice() {
        final Product product = new Product("후라이드", BigDecimal.valueOf(16000));
        final MenuProduct menuProduct = new MenuProduct(product, 5L);
        final MenuGroup menuGroup = new MenuGroup("신메뉴");

        assertThatThrownBy(() -> new Menu("두마리메뉴", null, menuGroup, List.of(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 0원보다 적은 경우 등록할 수 없다.")
    @Test
    void createMenuWithPriceLessThanZero() {
        final Product product = new Product("후라이드", BigDecimal.valueOf(16000));
        final MenuProduct menuProduct = new MenuProduct(product, 5L);
        final MenuGroup menuGroup = new MenuGroup("신메뉴");

        assertThatThrownBy(() -> new Menu("두마리메뉴", BigDecimal.valueOf(-1), menuGroup, List.of(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 해당 메뉴에 속한 메뉴 상품들의 가격의 총합보다 크면 등록할 수 없다.")
    @Test
    void createMenuWithIncorrectPrice() {
        final Product product = new Product("후라이드", BigDecimal.valueOf(16000));
        final MenuProduct menuProduct = new MenuProduct(product, 1L);
        final MenuGroup menuGroup = new MenuGroup("신메뉴");

        assertThatThrownBy(() -> new Menu("두마리메뉴", BigDecimal.valueOf(16001), menuGroup, List.of(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
