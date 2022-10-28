package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuTest {

    @DisplayName("가격이 null이면 예외를 발생시킨다.")
    @Test
    void priceIsNull_exception() {
        // given
        List<MenuProduct> menuProducts = List.of(
                new MenuProduct(1L, 3),
                new MenuProduct(1L, 3)
        );

        assertThatThrownBy(() -> new Menu("상품1", null, 1L, menuProducts))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 0보다 작으면 예외를 발생시킨다.")
    @Test
    void priceLessThanZero_exception() {
        // given
        List<MenuProduct> menuProducts = List.of(
                new MenuProduct(1L, 3),
                new MenuProduct(1L, 3)
        );

        assertThatThrownBy(() -> new Menu("상품1", BigDecimal.valueOf(-1L), 1L, menuProducts))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품들의 (가격 * 개수) 합보다 메뉴의 가격이 크면 예외를 발생시킨다.")
    @Test
    void priceMoreThanSumOfProducts_exception() {
        // given
        List<MenuProduct> menuProducts = List.of(
                new MenuProduct(1L, 3, BigDecimal.valueOf(1000)),
                new MenuProduct(1L, 3, BigDecimal.valueOf(1000))
        );

        assertThatThrownBy(() -> new Menu("상품1", BigDecimal.valueOf(7000), 1L, menuProducts))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
