package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Menu;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void 메뉴를_생성한다() {
        //given
        final BigDecimal price = BigDecimal.valueOf(2000);

        // when & then
        assertThatCode(() -> new Menu("메뉴", price, 1L))
                .doesNotThrowAnyException();
    }
}
