package kitchenpos.menu;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import kitchenpos.common.UnitTest;
import kitchenpos.menu.MenuProduct;
import org.junit.jupiter.api.Test;

@UnitTest
class MenuProductTest {

    @Test
    void menuProduct를_생성한다() {
        MenuProduct actual = new MenuProduct(1L, 1L, BigDecimal.valueOf(16000));

        assertAll(() -> {
            assertThat(actual.getProductId()).isEqualTo(1L);
            assertThat(actual.getQuantity()).isEqualTo(1L);
        });
    }

    @Test
    void menuProduct의_amount를_계산한다() {
        MenuProduct menuProduct = new MenuProduct(1L, 1L, BigDecimal.valueOf(16000));

        BigDecimal actual = menuProduct.getAmount();

        assertThat(actual.compareTo(BigDecimal.valueOf(16000))).isEqualTo(0);
    }
}
