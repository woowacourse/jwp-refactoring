package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Nested
    class changeEmpty {

        @Test
        void 테이블의_empty상태를_바꿀_수_있다() {
            // given
            final var table = new OrderTable(3, true);

            // when
            table.changeEmpty(false);

            // then
            assertThat(table.isEmpty()).isFalse();
        }

        @Test
        void 테이블그룹을_가지는_테이블이면_바꿀_수_없다() {
            // given
            final var tableGroupId = 1L;
            final var table = new OrderTable(tableGroupId, 3, true);

            // when & then
            assertThatThrownBy(() -> table.changeEmpty(true))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블 그룹에 포함된 테이블은 empty 상태를 변경할 수 없습니다.");
        }
    }

    @Nested
    class changeNumberOfGuests {

        @Test
        void 테이블의_numberOfGuests를_바꿀_수_있다() {
            // given
            final var table = new OrderTable(3, false);

            // when
            table.changeNumberOfGuests(5);

            // then
            assertThat(table.getNumberOfGuests()).isEqualTo(5);
        }

        @Test
        void 바꾸고자_하는_numberOfGuests가_0미만이면_바꿀_수_없다() {
            // given
            final var table = new OrderTable(3, false);

            // when & then
            assertThatThrownBy(() -> table.changeNumberOfGuests(-1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("바꾸려는 손님 수는 0명 이상이어야 합니다.");
        }

        @Test
        void 테이블이_비어있으면_바꿀_수_없다() {
            // given
            final var table = new OrderTable(3, true);

            // when & then
            assertThatThrownBy(() -> table.changeNumberOfGuests(5))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("빈 테이블은 손님 수를 바꿀 수 없습니다.");
        }
    }

    @Nested
    class group {

        @Test
        void 테이블을_그룹화_할_수_있다() {
            // given
            final var table = new OrderTable(3, true);
            final var tableGroupId = 1L;

            // when
            table.group(tableGroupId);

            // then
            assertThat(table.getTableGroupId()).isNotNull();
            assertThat(table.isEmpty()).isFalse();
        }

        @Test
        void 빈_테이블이_아니면_그룹화_할_수_없다() {
            // given
            final var table = new OrderTable(3, false);
            final var tableGroupId = 1L;

            // when & then
            assertThatThrownBy(() -> table.group(tableGroupId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("빈 테이블만 그룹화 할 수 있습니다.");
        }

        @Test
        void 이미_테이블그룹이_있으면_그룹화_할_수_없다() {
            // given
            final var tableGroupId1 = 1L;
            final var table = new OrderTable(tableGroupId1, 3, true);

            final var tableGroupId2 = 2L;

            // when & then
            assertThatThrownBy(() -> table.group(tableGroupId2))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 테이블 그룹에 포함된 테이블입니다.");
        }
    }

    @Nested
    class unGroup {

        @Test
        void 그룹화를_해제할_수_있다() {
            // given
            final var tableGroupId = 1L;
            final var table = new OrderTable(tableGroupId, 3, true);

            // when
            table.unGroup();

            // then
            assertThat(table.getTableGroupId()).isNull();
            assertThat(table.isEmpty()).isFalse();
        }
    }

}
