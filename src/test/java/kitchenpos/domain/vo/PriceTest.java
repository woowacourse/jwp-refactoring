package kitchenpos.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PriceTest {

    @ParameterizedTest
    @ValueSource(ints = {100, 1_000, 10_000})
    void 금액_객체를_생성한다(int value) {
        Price createdPrice = Price.of(value);

        assertThat(createdPrice.getValue()).isEqualTo(BigDecimal.valueOf(value));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void 값이_0보다_작으면_예외가_발생한다(int value) {
        assertThatThrownBy(() -> Price.of(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("금액은 음수가 될 수 없습니다.");
    }
}
