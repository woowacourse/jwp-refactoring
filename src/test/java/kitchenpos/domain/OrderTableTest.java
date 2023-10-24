package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
            final var table = new OrderTable(new TableGroup(), 3, true);

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

            // when
            table.group(new TableGroup());

            // then
            assertThat(table.getTableGroup()).isNotNull();
            assertThat(table.isEmpty()).isFalse();
        }

        @Test
        void 빈_테이블이_아니면_그룹화_할_수_없다() {
            // given
            final var table = new OrderTable(3, false);

            // when & then
            assertThatThrownBy(() -> table.group(new TableGroup()));
        }

        @Test
        void 이미_테이블그룹이_있으면_그룹화_할_수_없다() {
            // given
            final var table = new OrderTable(new TableGroup(), 3, true);

            // when & then
            assertThatThrownBy(() -> table.group(new TableGroup()));
        }
    }

    @Nested
    class unGroup {

        @Test
        void 그룹화를_해제할_수_있다() {
            // given
            final var table = new OrderTable(new TableGroup(), 3, true);

            // when
            table.unGroup();

            // then
            assertThat(table.getTableGroup()).isNull();
            assertThat(table.isEmpty()).isFalse();
        }
    }

}
