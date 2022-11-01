package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuTest {

    @DisplayName("메뉴 생성 시 메뉴의 가격이 null이면 예외가 발생한다.")
    @Test
    void constructWithNullPrice() {
        assertThatThrownBy(
                () -> new Menu("1번 메뉴", null, 1L,
                        new MenuProducts(Arrays.asList(new MenuProduct(1L, 1L, BigDecimal.valueOf(10000))), BigDecimal.valueOf(9000)))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 시 메뉴의 가격이 0보다 작으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -5})
    void constructWithInvalidPrice(int price) {
        assertThatThrownBy(
                () -> new Menu("1번 메뉴", BigDecimal.valueOf(price), 1L,
                        new MenuProducts(Arrays.asList(new MenuProduct(1L, 1L, BigDecimal.valueOf(10000))), BigDecimal.valueOf(9000)))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 시 메뉴상품이 비어 있으면 예외가 발생한다.")
    @Test
    void constructWithNoMenuProduct() {
        assertThatThrownBy(() -> new Menu("1번 메뉴", BigDecimal.valueOf(10000), 1L, new MenuProducts(new ArrayList<>(), BigDecimal.valueOf(9000))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 시 메뉴의 가격이 메뉴 상품의 총합 보다 크면 예외가 발생한다.")
    @ValueSource(ints = {10001, 50000})
    @ParameterizedTest
    void constructWithPriceMoreThanMenuProductSum(int price) {
        assertThatThrownBy(
                () -> new Menu("1번 메뉴", BigDecimal.valueOf(price), 1L,
                        new MenuProducts(Arrays.asList(new MenuProduct(1L, 1L, BigDecimal.valueOf(10000))), BigDecimal.valueOf(price)))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
