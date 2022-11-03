package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.Test;

class MenuTest {
    @Test
    void 생성() {
        assertThatThrownBy(() ->
                new Menu(
                        "후라이드",
                        BigDecimal.valueOf(1100L),
                        1L,
                        Arrays.asList(new MenuProduct(1L, 1, BigDecimal.valueOf(1000L))))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
