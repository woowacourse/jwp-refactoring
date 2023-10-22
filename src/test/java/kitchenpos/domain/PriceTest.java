package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PriceTest {

    @Test
    void 가격은_null이_아니다() {
        // expect
        assertThatThrownBy(() -> new Price(null));
    }

    @Test
    void 가격은_0_이하면_예외() {
        // expect
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-100)));
    }
}
