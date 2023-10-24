package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class OrderTableTest {

    @DisplayName("주문 테이블 생성 테스트")
    @Nested
    class OrderTableCreateTest {

        @DisplayName("주문 테이블을 생성한다.")
        @Test
        void orderTableCreateTest() {
            //given
            final int guests = 3;
            final boolean empty = true;

            //when
            final OrderTable orderTable = new OrderTable(null, guests, empty);

            //then
            assertSoftly(softly -> {
                softly.assertThat(orderTable.getId()).isNull();
                softly.assertThat(orderTable.getNumberOfGuests()).isEqualTo(guests);
                softly.assertThat(orderTable.isEmpty()).isEqualTo(empty);
            });
        }
    }

    @DisplayName("주문 테이블을 수정한다.")
    @Nested
    class OrderTableChangeTest {

        @DisplayName("테이블 그룹을 수정한다.")
        @Test
        void settingTableGroupTest() {
            //given
            final int guests = 3;
            final boolean empty = true;
            final TableGroup afterTableGroup = new TableGroup(LocalDateTime.now(), List.of(new OrderTable(null, 3, true), new OrderTable(null, 3, true)));
            final OrderTable orderTable = new OrderTable(null, guests, empty);

            //when
            orderTable.joinTableGroupById(afterTableGroup.getId());

            //then
            assertSoftly(softly -> {
                softly.assertThat(orderTable.getId()).isNull();
                softly.assertThat(orderTable.getTableGroupId()).isEqualTo(afterTableGroup.getId());
                softly.assertThat(orderTable.getNumberOfGuests()).isEqualTo(guests);
                softly.assertThat(orderTable.isEmpty()).isEqualTo(empty);
            });
        }

        @DisplayName("비어있는지를 수정한다.")
        @Test
        void changeEmptyTest() {
            //given
            final int guests = 3;
            final boolean empty = true;
            final boolean afterEmpty = false;
            final OrderTable orderTable = new OrderTable(null, guests, empty);

            //when
            orderTable.changeEmpty(afterEmpty);

            //then
            assertSoftly(softly -> {
                softly.assertThat(orderTable.getId()).isNull();
                softly.assertThat(orderTable.getNumberOfGuests()).isEqualTo(guests);
                softly.assertThat(orderTable.isEmpty()).isEqualTo(afterEmpty);
            });
        }

        @DisplayName("손님 수를 수정한다.")
        @Test
        void changeNumberOfGuestsTest() {
            //given
            final int guests = 3;
            final boolean empty = false;
            final int afterGuests = 10;
            final OrderTable orderTable = new OrderTable(null, guests, empty);

            //when
            orderTable.changeNumberOfGuests(afterGuests);

            //then
            assertSoftly(softly -> {
                softly.assertThat(orderTable.getNumberOfGuests()).isEqualTo(afterGuests);
            });
        }

        @DisplayName("손님 수를 수정할 때 음수면 실패한다.")
        @Test
        void changeNumberOfGuestsFailWhenGuestsLessThenZero() {
            //given
            final int guests = 3;
            final boolean empty = false;
            final int afterGuests = -1;
            final OrderTable orderTable = new OrderTable(null, guests, empty);

            // when & then
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(afterGuests))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("손님 수를 수정할 때 테이블이 비어있으면 실패한다.")
        @Test
        void changeNumberOfGuestsFailWhenGuestsEmpty() {
            //given
            final int guests = 3;
            final boolean empty = true;
            final int afterGuests = -1;
            final OrderTable orderTable = new OrderTable(null, guests, empty);

            // when & then
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(afterGuests))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("그룹화 테스트")
    @Nested
    class OrderTableIsGroupableTest {

        @DisplayName("그룹화 하려면 비어있으면서 테이블 그룹이 null이어야 한다.")
        @Test
        void isGroupableTest() {
            //given & when
            final OrderTable orderTable = new OrderTable(null, 3, true);

            //then
            assertSoftly(softly -> {
                softly.assertThat(orderTable.isGroupable()).isTrue();
            });
        }
    }
}
