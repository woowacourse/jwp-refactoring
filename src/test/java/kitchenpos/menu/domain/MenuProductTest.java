package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    void 가격_총합을_계산한다() {
        MenuProduct menuProduct = new MenuProduct(1L, 6, BigDecimal.valueOf(5_000));
        BigDecimal actual = menuProduct.calculateAmount();
        assertThat(actual).isEqualTo(BigDecimal.valueOf(30_000));
    }
}
