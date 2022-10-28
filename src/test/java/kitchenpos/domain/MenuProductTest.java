package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuProductTest {

    @DisplayName("amount 가격을 알 수 있다.")
    @Test
    void calculateAmount() {
        // given
        MenuProduct menuProduct = new MenuProduct(1L, 2, BigDecimal.valueOf(1000));

        // when
        BigDecimal amount = menuProduct.getAmount();

        // then
        assertThat(amount).isEqualTo(BigDecimal.valueOf(2000));
    }
}
