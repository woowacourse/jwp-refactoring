package kitchenpos.domain.common;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.exception.InvalidNumberOfGuestsException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class NumberOfGuestsTest {

    @Test
    void 생성자는_유효한_손님_수를_전달하면_NumberOfGuests를_초기화한다() {
        // when & then
        assertThatCode(() -> new NumberOfGuests(1)).doesNotThrowAnyException();
    }

    @ParameterizedTest(name = "손님 수가 {0}이면 예외가 발생한다.")
    @ValueSource(ints = {-1 -2, -3})
    void 생성자는_손님_수로_음수를_전달하면_예외가_발생한다(final int invalidNumberOfGuests) {
        // given
        assertThatThrownBy(() -> new NumberOfGuests(invalidNumberOfGuests))
                .isInstanceOf(InvalidNumberOfGuestsException.class);
    }
}
