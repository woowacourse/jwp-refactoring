package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTableTest {

    @Test
    void 빈_테이블로_변경할_때_이미_단체_지정된_경우_예외가_발생한다() {
        // given
        final var orderTable = OrderTableFixture.빈테이블_1명_단체지정();

        // when & then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님_수를_변경할_때_빈_테이블인_경우_예외가_발생한다() {
        // given
        final var orderTable = OrderTableFixture.빈테이블_1명();

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(10))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
