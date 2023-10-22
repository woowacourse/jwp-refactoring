package kitchenpos.domain;

import static kitchenpos.domain.Quantity.valueOf;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class QuantityTest {

    @Test
    void 개수를_생성할_때_값이_0_미만이면_예외를_던진다() {
        // given
        long invalidValue = -1L;

        // expect
        assertThatThrownBy(() -> valueOf(invalidValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("개수가 0 미만일 수 없습니다.");
    }

    @Test
    void 정상적으로_생성된다() {
        // given
        long value = 0L;

        // expect
        assertThatNoException().isThrownBy(() -> Quantity.valueOf(value));
    }
}
