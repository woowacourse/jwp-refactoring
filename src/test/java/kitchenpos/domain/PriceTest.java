package kitchenpos.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.PriceEmptyException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PriceTest {

    @Test
    void 가격은_0원_미만이면_예외를_발생시킨다() {
        // given
        Long price = -100L;

        // when & then
        assertThatThrownBy(() -> Price.from(price))
                .isInstanceOf(PriceEmptyException.class);
     }
}
