package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.domain.common.Price;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    void 금액을_계산할_수_있다() {
        MenuProduct menuProduct = new MenuProduct(1L, 10, new Price(BigDecimal.ONE));

        Price amount = menuProduct.getAmount();

        assertThat(amount).isEqualTo(new Price(BigDecimal.TEN));
    }
}
