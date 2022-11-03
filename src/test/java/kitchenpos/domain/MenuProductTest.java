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
        MenuProduct menuProduct = new MenuProduct(new Product("상품1", 2000L), 2);

        // when
        BigDecimal amount = menuProduct.getAmount();

        // then
        assertThat(amount).isEqualTo(BigDecimal.valueOf(4000));
    }
}
