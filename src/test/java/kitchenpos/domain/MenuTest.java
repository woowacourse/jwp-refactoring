package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("메뉴 도메인 테스트")
class MenuTest {

    @DisplayName("[실패] 총 MenuProducts의 값보다 price가 크다면 메뉴 생성 불가")
    @Test
    void menu_InvalidPrice_ExceptionThrown() {
        // given
        List<MenuProduct> menuProducts = Arrays.asList(
            menuProduct(BigDecimal.valueOf(10), 2L),
            menuProduct(BigDecimal.valueOf(20), 1L)
        );

        BigDecimal price = menuProducts.stream()
            .map(this::calculateTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .add(BigDecimal.TEN);

        // when
        // then
        assertThatThrownBy(() -> new Menu(
            "메뉴", price, new MenuGroup("메뉴그룹"), menuProducts
        )).isInstanceOf(InvalidPriceException.class);
    }

    private BigDecimal calculateTotalPrice(MenuProduct menuProduct) {
        return menuProduct.getProduct().getPrice()
            .multiply(BigDecimal.valueOf(menuProduct.getQuantity()));
    }

    private MenuProduct menuProduct(BigDecimal price, Long quantity) {
        return new MenuProduct(new Product("상품", price), quantity);
    }

    @DisplayName("[실패] MenuPrice가 음수라면 생성 불가")
    @ParameterizedTest
    @ValueSource(ints = {-1, -10})
    void menuPrice_negativePrice_ExceptionThrown(int priceAsInt) {
        // given
        BigDecimal price = BigDecimal.valueOf(priceAsInt);

        // when
        // then
        assertThatThrownBy(() -> new MenuPrice(price))
            .isInstanceOf(InvalidPriceException.class);
    }

    @DisplayName("[실패] MenuPrice가 null이라면 생성 불가")
    @Test
    void menuPrice_nullPrice_ExceptionThrown() {
        // given
        // when
        // then
        assertThatThrownBy(() -> new MenuPrice(null))
            .isInstanceOf(InvalidPriceException.class);
    }
}
