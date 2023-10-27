package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import kitchenpos.table.domain.model.OrderTable;
import kitchenpos.table.supports.OrderTableFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTableTest {

    @Test
    void 그룹_해제() {
        // given
        OrderTable orderTable = OrderTableFixture.fixture().empty(true).tableGroupId(1L).build();

        // when
        orderTable.ungroup();

        // then
        assertSoftly(softly -> {
            softly.assertThat(orderTable.getTableGroupId()).isNull();
            softly.assertThat(orderTable.isEmpty()).isFalse();
        });
    }

    @Nested
    class 손님_수_변경 {

        @Test
        void 손님수가_음수이면_예외() {
            // given
            OrderTable orderTable = OrderTableFixture.fixture().empty(false).build();

            // when & then
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님 수는 음수일 수 없습니다.");
        }

        @Test
        void 빈테이블이면_예외() {
            // given
            OrderTable orderTable = OrderTableFixture.fixture().empty(true).build();

            // when & then
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비어있는 주문 테이블입니다.");
        }

        @Test
        void 손님_수_변경() {
            // given
            OrderTable orderTable = OrderTableFixture.fixture().empty(false).numberOfGuests(0).build();

            // when
            orderTable.changeNumberOfGuests(10);

            // then
            assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
        }
    }
}
