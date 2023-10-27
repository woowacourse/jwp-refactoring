package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("메뉴의 가격이 상품 가격들의 합보다 크지 않은지 확인한다.")
    @Test
    void validateMenuPrice() {
        // given
        final Menu menu = new Menu("치킨 세트", new Price(BigDecimal.valueOf(30000)), 1L);

        // when & then
        assertDoesNotThrow(() -> menu.validateMenuPrice(new Price(BigDecimal.valueOf(32000))));
    }

    @DisplayName("메뉴의 가격이 상품 가격들의 합보다 크면 예외 처리한다.")
    @Test
    void validateMenuPrice_FailWhenGraterThanProductSum() {
        // given
        final Menu menu = new Menu("치킨 세트", new Price(BigDecimal.valueOf(30000)), 1L);

        assertThatThrownBy(() -> menu.validateMenuPrice(new Price(BigDecimal.valueOf(29000))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격이 상품 가격들의 합보다 큽니다.");

    }
}
