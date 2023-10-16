package kitchenpos.domain.product;

import kitchenpos.exception.KitchenposException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.exception.ExceptionInformation.PRODUCT_PRICE_LENGTH_OUT_OF_BOUNCE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("상품가격 테스트")
class PriceTest {

    @Test
    void 상품_가격은_음수일수_없다() {
        assertThatThrownBy(() -> Price.from(new BigDecimal(-1)))
                .isExactlyInstanceOf(KitchenposException.class)
                .hasMessage(PRODUCT_PRICE_LENGTH_OUT_OF_BOUNCE.getMessage());
    }

    @Test
    void 상품_가격이_int의_최대값보다_클_수_없다() {
        final long outOfIntBound = ((long) Integer.MAX_VALUE + 1);
        assertThatThrownBy(() -> Price.from(new BigDecimal(outOfIntBound)))
                .isExactlyInstanceOf(KitchenposException.class)
                .hasMessage(PRODUCT_PRICE_LENGTH_OUT_OF_BOUNCE.getMessage());
    }

}
