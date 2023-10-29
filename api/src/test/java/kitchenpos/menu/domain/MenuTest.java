package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {
    @Test
    @DisplayName("메뉴를 생성한다.")
    void createMenu() {
        // given
        final String name = "피자";
        final BigDecimal price = new BigDecimal("15000");
        final Long menuGroupId = 1L;
        final List<MenuProduct> menuProducts = Collections.singletonList(new MenuProduct());

        // when
        final Menu menu = Menu.of(name, price, menuGroupId, menuProducts);

        // then
        assertThat(menu.getName()).isEqualTo(name);
        assertThat(menu.getPrice()).isEqualTo(price);
    }

    @Test
    @DisplayName("메뉴 이름이 null이거나 빈 문자열인 경우 예외를 던진다.")
    void throwExceptionIfMenuNameIsEmptyOrNull() {
        // given
        // when
        final String name = "";
        final BigDecimal price = new BigDecimal("15000");
        final Long menuGroupId = 1L;
        final List<MenuProduct> menuProducts = List.of(new MenuProduct());

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Menu.of(name, price, menuGroupId, menuProducts));
    }

    @Test
    @DisplayName("메뉴 이름이 64자를 초과하는 경우 예외를 던진다.")
    void throwExceptionIfMenuNameIsTooLong() {
        // given
        final String name = "a".repeat(65);
        final BigDecimal price = new BigDecimal("15000");
        final Long menuGroupId = 1L;
        final List<MenuProduct> menuProducts = List.of(new MenuProduct());

        // when
        // then
        assertThatThrownBy(
                () -> Menu.of(name, price, menuGroupId, menuProducts)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 가격이 null이거나 음수인 경우 예외를 던진다.")
    void throwExceptionIfPriceIsNullOrNegative() {
        // given
        final String name = "피자";
        final BigDecimal price = new BigDecimal("-15000");
        final Long menuGroupId = 1L;
        final List<MenuProduct> menuProducts = Collections.singletonList(new MenuProduct());

        // when
        // then
        assertThatThrownBy(
                () -> Menu.of(name, price, menuGroupId, menuProducts)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 가격이 양수인 경우 정상적으로 생성한다.")
    void createMenuIfPriceIsPositive() {
        // given
        final String name = "피자";
        final BigDecimal price = BigDecimal.ZERO;
        final Long menuGroupId = 1L;
        final List<MenuProduct> menuProducts = Collections.singletonList(new MenuProduct());

        // when
        final Menu menu = Menu.of(name, price, menuGroupId, menuProducts);

        // then
        assertThat(menu.getPrice()).isEqualTo(BigDecimal.ZERO);
    }
}
