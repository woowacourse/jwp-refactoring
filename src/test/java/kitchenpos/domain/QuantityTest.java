package kitchenpos.domain;

import kitchenpos.common.domain.Quantity;
import kitchenpos.common.exception.QuantityEmptyException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class QuantityTest {

    @Test
    void 수량은_0개_미만이면_예외를_발생한다() {
        // given
        Long quantity = -1L;

        // when & then
        assertThatThrownBy(() -> Quantity.from(quantity))
                .isInstanceOf(QuantityEmptyException.class);
    }
}
