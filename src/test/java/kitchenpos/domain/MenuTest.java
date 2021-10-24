package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Menu 엔티티 단위 테스트")
class MenuTest {

    @DisplayName("Menu는 가격이 null이면 생성 예외가 발생한다.")
    @Test
    void newMenu_PriceNull_Exception() {
        // given, when, then
        assertThatCode(() -> new Menu("kevin", null, new MenuGroup()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("유효하지 않은 Menu 가격입니다.");
    }

    @DisplayName("Menu는 가격이 0 미만인 경우 생성 예외가 발생한다.")
    @Test
    void newMenu_PriceNegative_Exception() {
        // given, when, then
        assertThatCode(() -> new Menu("kevin", BigDecimal.valueOf(-1), new MenuGroup()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("유효하지 않은 Menu 가격입니다.");
    }

    @DisplayName("Menu는 가격이 0 이상 경우 정상 생성된다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10})
    void newMenu_PriceNegative_Exception(int price) {
        // given, when, then
        assertThatCode(() -> {
            Menu Menu = new Menu("kevin", BigDecimal.valueOf(price), new MenuGroup());
            assertThat(Menu.getPrice()).isEqualTo(BigDecimal.valueOf(price));
        }).doesNotThrowAnyException();
    }

    @DisplayName("Menu 가격이 Product 가격 누계를 초과하면 MenuProduct 업데이트가 불가능하다.")
    @Test
    void updateMenuProducts_PriceBiggerThanProductSum_Exception() {
        // given
        MenuProduct menuProduct1 = new MenuProduct(null, new Product("a", BigDecimal.valueOf(10)), 3);
        MenuProduct menuProduct2 = new MenuProduct(null, new Product("b", BigDecimal.valueOf(30)), 2);
        Menu menu = new Menu("kevin", BigDecimal.valueOf(91), new MenuGroup());

        // when, then
        assertThatCode(() -> menu.updateMenuProducts(Arrays.asList(menuProduct1, menuProduct2)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Menu 가격은 Product 가격 누계를 초과할 수 없습니다.");
    }


    @DisplayName("Menu 가격이 Product 가격 누계 이하면 MenuProduct 업데이트가 불가능하다.")
    @ParameterizedTest
    @ValueSource(ints = {89, 90})
    void updateMenuProducts_PriceSmallerOrEqualThanProductSum_Exception(int value) {
        // given
        MenuProduct menuProduct1 = new MenuProduct(null, new Product("a", BigDecimal.valueOf(10)), 3);
        MenuProduct menuProduct2 = new MenuProduct(null, new Product("b", BigDecimal.valueOf(30)), 2);
        Menu menu = new Menu("kevin", BigDecimal.valueOf(value), new MenuGroup());

        // when
        menu.updateMenuProducts(Arrays.asList(menuProduct1, menuProduct2));

        // then
        assertThat(menu.getMenuProducts()).hasSize(2);
    }
}
