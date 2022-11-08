package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("가격이 null이면 예외를 반환한다.")
    @Test
    void create_exception_priceIsNull() {
        assertThatThrownBy(() -> new Menu("마이쮸 포도맛", null, 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 0보다 작으면 예외를 반환한다.")
    @Test
    void create_exception_priceIsLessThanZero() {
        assertThatThrownBy(() -> new Menu("마이쮸 포도맛", BigDecimal.valueOf(-1), 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 총 가격보다 크면 예외를 반환한다.")
    @Test
    void create_exception_priceIsGreaterThanAmount() {
        assertThatThrownBy(() -> new Menu(
                "마이쮸 포도맛",
                BigDecimal.valueOf(2000),
                1L,
                List.of(new MenuProduct(1L, 1, BigDecimal.valueOf(1000)))
        ))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
