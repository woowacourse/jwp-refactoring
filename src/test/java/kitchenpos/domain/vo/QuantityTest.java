package kitchenpos.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class QuantityTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10, 100})
    void 수량_객체를_생성한다(int value) {
        Quantity quantity = new Quantity(value);

        assertThat(quantity.getValue()).isEqualTo(value);
    }

    @Test
    void 수량이_음수이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Quantity(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수량은 0보다 작을 수 없습니다.");
    }
}
