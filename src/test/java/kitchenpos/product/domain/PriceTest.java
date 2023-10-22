package kitchenpos.product.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class PriceTest {

    @ValueSource(longs = {-1, -100, -10000})
    @ParameterizedTest(name = "가격은 {0} 일 수 없다.")
    void 가격은_음수일_수_없다(final long price) {
        // given & when & then
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> Price.from(price)));
    }
}
