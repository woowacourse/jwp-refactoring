package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    @DisplayName("가격의 총합을 계산한다.")
    void calculateAmount() {
        final MenuProduct product = new MenuProduct(1L, 5L, BigDecimal.valueOf(2000L));

        assertThat(product.getAmount()).isEqualTo(BigDecimal.valueOf(10000L));
    }
}
