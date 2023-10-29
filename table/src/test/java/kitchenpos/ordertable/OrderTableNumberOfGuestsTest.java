package kitchenpos.ordertable;

import kitchenpos.table.domain.OrderTableNumberOfGuests;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTableNumberOfGuestsTest {
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void 방문자_수는_0_이상이다(int numberOfQuests) {
        final OrderTableNumberOfGuests orderTableNumberOfGuests = new OrderTableNumberOfGuests(numberOfQuests);
        assertThat(orderTableNumberOfGuests.getNumberOfGuests()).isEqualTo(numberOfQuests);
    }

    @Test
    void 방문자_수가_음수이면_예외가_발생한다() {
        assertThatThrownBy(() -> new OrderTableNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
