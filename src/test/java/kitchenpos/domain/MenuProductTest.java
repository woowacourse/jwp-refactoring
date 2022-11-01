package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuProductTest {

    @Test
    @DisplayName("메뉴 상품의 총 합산 가격을 계산한다.")
    void amount() {
        // given
        final MenuProduct menuProduct = new MenuProduct(1L, 1L, 1L, new BigDecimal(1000L), 3L);

        // when
        final BigDecimal amount = menuProduct.calculateTotalAmount();

        // then
        assertThat(amount.longValue()).isEqualTo(3_000L);
    }
}
