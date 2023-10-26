package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import kitchenpos.table.domain.model.OrderTable;
import kitchenpos.table.domain.model.TableGroup;
import kitchenpos.table.supports.OrderTableFixture;
import kitchenpos.table.supports.TableGroupFixture;
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
        TableGroup tableGroup = TableGroupFixture.fixture().build();
        OrderTable orderTable = OrderTableFixture.fixture().empty(true).tableGroup(tableGroup).build();

        // when
        orderTable.ungroup();

        // then
        assertSoftly(softly -> {
            softly.assertThat(orderTable.getTableGroup()).isNull();
            softly.assertThat(orderTable.isEmpty()).isFalse();
        });
    }

    @Nested
    class 그룹_지정 {

        @Test
        void 빈_테이블이_아니면_예외() {
            // given
            OrderTable orderTable = OrderTableFixture.fixture().empty(false).build();
            TableGroup tableGroup = TableGroupFixture.fixture().build();

            // when & then
            assertThatThrownBy(() -> orderTable.group(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 이미_그룹지정된_테이블이면_예외() {
            // given
            TableGroup tableGroup1 = TableGroupFixture.fixture().id(1L).build();
            TableGroup tableGroup2 = TableGroupFixture.fixture().id(2L).build();
            OrderTable orderTable = OrderTableFixture.fixture().empty(true).tableGroup(tableGroup1).build();

            // when & then
            assertThatThrownBy(() -> orderTable.group(tableGroup2))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 성공() {
            // given
            TableGroup tableGroup = TableGroupFixture.fixture().build();
            OrderTable orderTable = OrderTableFixture.fixture().empty(true).tableGroup(null).build();

            // when
            orderTable.group(tableGroup);

            // then
            assertSoftly(softly -> {
                softly.assertThat(orderTable.getTableGroup()).isEqualTo(tableGroup);
                softly.assertThat(orderTable.isEmpty()).isFalse();
            });
        }
    }

    @Nested
    class 그룹_해제_가능한지_확인 {

        @Test
        void 테이블_그룹이_없으면_거짓() {
            // given
            OrderTable orderTable = OrderTableFixture.fixture().empty(false).tableGroup(null).build();

            // when
            boolean result = orderTable.isAbleToUngroup();

            // then
            assertThat(result).isFalse();
        }

        @Test
        void 빈_테이블이면_거짓() {
            // given
            TableGroup tableGroup = TableGroupFixture.fixture().id(1L).build();
            OrderTable orderTable = OrderTableFixture.fixture().empty(true).tableGroup(tableGroup).build();

            // when
            boolean result = orderTable.isAbleToUngroup();

            // then
            assertThat(result).isFalse();
        }
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
