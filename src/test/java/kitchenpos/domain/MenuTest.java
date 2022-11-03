package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("메뉴의 가격이 비어있을 경우 예외를 발생한다.")
    @Test
    void nullPriceThrowException() {

        assertThatThrownBy(
                () -> new Menu("메뉴 이름", null, 1L,
                        Collections.singletonList(new MenuProduct(1L, 1, BigDecimal.ZERO)))
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴의 가격은 비어있거나 0보다 작을 수 없습니다.");
    }

    @DisplayName("메뉴의 가격이 0원 미만일 경우 예외를 발생한다.")
    @Test
    void Price0ThrowException() {

        assertThatThrownBy(
                () -> new Menu("메뉴 이름", BigDecimal.valueOf(-1), 1L,
                        Collections.singletonList(new MenuProduct(1L, 1, BigDecimal.ZERO)))
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴의 가격은 비어있거나 0보다 작을 수 없습니다.");
    }

    @DisplayName("메뉴의 가격이 상품 가격 합계보다 비쌀 경우 예외를 발생한다.")
    @Test
    void invalidPriceThrowException() {

        assertThatThrownBy(
                () -> new Menu("메뉴 이름", BigDecimal.valueOf(1000), 1L,
                        Collections.singletonList(new MenuProduct(1L, 1, BigDecimal.ZERO)))
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격이 유효하지 않습니다.");
    }

}
