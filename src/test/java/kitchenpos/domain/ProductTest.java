package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class ProductTest {

    @Test
    void 상품이_정상적으로_생성된다() {
        // given
        final BigDecimal rawPrice = BigDecimal.valueOf(5_000L);

        // when, then
        assertThatCode(() -> new Product("치킨", rawPrice))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(longs = {-10_000, -5_000, -1_000, -500, -1})
    void 상품_가격이_음수인_경우_예외가_발생한다(final Long rawPrice) {
        // given,  when, then
        assertThatThrownBy(() -> new Product("치킨", BigDecimal.valueOf(rawPrice)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
