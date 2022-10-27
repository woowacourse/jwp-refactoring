package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MenuProduct 는 ")
class MenuProductTest {

    @DisplayName("단일 메뉴 상품의 금액을 계산한다.")
    @Test
    void calculateAmount() {
        final Product product = new Product(1L, "proudctName", BigDecimal.valueOf(1000L));
        final MenuProduct menuProduct = new MenuProduct(1L, 1L, product, 2L);
        final BigDecimal amount = menuProduct.calculateAmount();

        assertThat(amount.longValue()).isEqualTo(2000L);
    }
}
