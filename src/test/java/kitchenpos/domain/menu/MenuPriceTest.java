package kitchenpos.domain.menu;

import kitchenpos.BaseTest;
import kitchenpos.exception.MenuPriceException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class MenuPriceTest extends BaseTest {

    @Test
    void 메뉴_가격을_생성한다() {
        // given
        BigDecimal price = BigDecimal.valueOf(1000L);

        // when, then
        Assertions.assertThatNoException()
                .isThrownBy(() -> new MenuPrice(price));
    }

    @Test
    void 메뉴_가격이_null_이면_예외를_던진다() {
        // given
        BigDecimal price = null;

        // when, then
        Assertions.assertThatThrownBy(() -> new MenuPrice(price))
                .isInstanceOf(MenuPriceException.class);
    }

    @Test
    void 메뉴_가격이_음수_이면_예외를_던진다() {
        // given
        BigDecimal price = BigDecimal.valueOf(-1000L);

        // when, then
        Assertions.assertThatThrownBy(() -> new MenuPrice(price))
                .isInstanceOf(MenuPriceException.class);
    }
}
