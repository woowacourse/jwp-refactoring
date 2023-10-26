package kitchenpos.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);

        // when
        orderTable.changeNumberOfGuests(2);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(2);
    }

    @Test
    void changeNumberOfGuests_emptyTrueException() {
        // given
        final OrderTable orderTable = new OrderTable(1, true);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests_invalidNumberException() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeEmpty() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);

        // when
        orderTable.changeEmpty(true);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    void changeEmpty_groupNotNullException() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);
        orderTable.joinTableGroup(new TableGroup());

        // when & then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void validateCanJoinTableGroup() {
        // given
        final OrderTable emptyOrderTable = new OrderTable(1, true);

        // when & then
        assertThatNoException().isThrownBy(emptyOrderTable::validateCanJoinTableGroup);
    }

    @Test
    void validateCanJoinTableGroup_emptyException() {
        // given
        final OrderTable notEmptyOrderTable = new OrderTable(1, false);

        // when & then
        assertThatThrownBy(notEmptyOrderTable::validateCanJoinTableGroup)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void joinTableGroup() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);
        final TableGroup tableGroup = new TableGroup();

        // when
        orderTable.joinTableGroup(tableGroup);

        // then
        assertSoftly(softly -> {
            softly.assertThat(orderTable.getTableGroup()).isEqualTo(tableGroup);
            softly.assertThat(orderTable.isEmpty()).isFalse();
        });
    }

    @Test
    void unjoinTableGroup() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);
        final TableGroup tableGroup = new TableGroup();
        orderTable.joinTableGroup(tableGroup);

        // when
        orderTable.unjoinTableGroup();

        // then
        assertSoftly(softly -> {
            softly.assertThat(orderTable.getTableGroup()).isNull();
            softly.assertThat(orderTable.isEmpty()).isFalse();
        });
    }
}
