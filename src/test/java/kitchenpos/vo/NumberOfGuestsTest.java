package kitchenpos.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.dto.vo.NumberOfGuests;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class NumberOfGuestsTest {

    @Test
    void 손님_수_객체를_생성한다() {
        int value = 5;

        NumberOfGuests numberOfGuests = NumberOfGuests.from(value);

        assertThat(numberOfGuests.getNumberOfGuests()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -10_000})
    void 손님_수가_1명_미만이면_예외가_발생한다(int value) {
        assertThatThrownBy(() -> NumberOfGuests.from(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님 수는 음수가 될 수 없습니다.");
    }
}
