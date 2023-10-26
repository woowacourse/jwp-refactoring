package kitchenpos.domain;

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
            assertThatThrownBy(() -> table.changeEmpty(true));
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
            assertThatThrownBy(() -> table.changeNumberOfGuests(-1));
        }

        @Test
        void 테이블이_비어있으면_바꿀_수_없다() {
            // given
            final var table = new OrderTable(3, true);

            // when & then
            assertThatThrownBy(() -> table.changeNumberOfGuests(5));
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
            assertThatThrownBy(() -> table.group(tableGroupId));
        }

        @Test
        void 이미_테이블그룹이_있으면_그룹화_할_수_없다() {
            // given
            final var tableGroupId1 = 1L;
            final var table = new OrderTable(tableGroupId1, 3, true);

            final var tableGroupId2 = 2L;

            // when & then
            assertThatThrownBy(() -> table.group(tableGroupId2));
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
