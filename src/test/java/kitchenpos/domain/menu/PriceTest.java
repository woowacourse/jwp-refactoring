package kitchenpos.domain.menu;


import kitchenpos.global.exception.KitchenposException;
import kitchenpos.menu.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.global.exception.ExceptionInformation.MENU_PRICE_LENGTH_OUT_OF_BOUNCE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("메뉴가격 테스트")
class PriceTest {

    @Test
    void 메뉴_가격은_음수일수_없다() {
        assertThatThrownBy(() -> Price.create(new BigDecimal(-1)))
                .isExactlyInstanceOf(KitchenposException.class)
                .hasMessage(MENU_PRICE_LENGTH_OUT_OF_BOUNCE.getMessage());
    }

    @Test
    void 메뉴_가격이_int의_최대값보다_클_수_없다() {
        final long outOfIntBound = ((long) Integer.MAX_VALUE + 1);
        assertThatThrownBy(() -> Price.create(new BigDecimal(outOfIntBound)))
                .isExactlyInstanceOf(KitchenposException.class)
                .hasMessage(MENU_PRICE_LENGTH_OUT_OF_BOUNCE.getMessage());
    }

    @Test
    void 메뉴_가격이_소수점_셋째짜리이상은_만들_수_없다() {
        assertThatThrownBy(() -> Price.create(new BigDecimal(3.333)))
                .isExactlyInstanceOf(KitchenposException.class)
                .hasMessage(MENU_PRICE_LENGTH_OUT_OF_BOUNCE.getMessage());
    }

}
