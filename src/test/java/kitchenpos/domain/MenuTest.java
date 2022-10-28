package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Menu 도메인 테스트")
class MenuTest {

    @DisplayName("메뉴의 가격은 주문 금액의 총 합보다 작거나 같아야 한다")
    @Test
    void priceIsHigherThanAmount() {
        final MenuProduct menuProduct = new MenuProduct(1L, 1, BigDecimal.valueOf(15_000));
        final BigDecimal menuPrice = BigDecimal.valueOf(16_000);

        assertThatThrownBy(() -> new Menu("후라이드 치킨 세트", menuPrice, 1L, List.of(menuProduct)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("메뉴의 가격은 상품의 총 합보다 같거나 작아야 합니다.");
    }
}
