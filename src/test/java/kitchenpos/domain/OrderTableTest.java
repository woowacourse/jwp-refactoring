package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithEmpty;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithNumberOfGuest;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithTableGroupId;
import static kitchenpos.fixture.TableGroupFixture.createTableGroupWithId;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OrderTableTest {

    @DisplayName("OrderTable 손님 수 0명 미만일 때 예외 발생")
    @Test
    void wrongCreate() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> createOrderTableWithNumberOfGuest(-1))
                .withMessage("손님 수는 음수일 수 없습니다.");
    }

    @DisplayName("OrderTable empty 여부 변경")
    @Test
    void changeEmpty() {
        OrderTable emptyOrderTable = createOrderTableWithEmpty(true);

        OrderTable actual = emptyOrderTable.changeEmpty(false);

        assertThat(actual.isEmpty()).isFalse();
    }

    @DisplayName("OrderTable TableGroup 변경")
    @Test
    void changeTableGroup() {
        OrderTable orderTable = createOrderTableWithTableGroupId(createTableGroupWithId(1L));
        TableGroup expected = createTableGroupWithId(2L);

        OrderTable actual = orderTable.changeTableGroup(expected);

        assertThat(actual.getTableGroup()).isEqualToComparingFieldByField(expected);
    }

    @DisplayName("OrderTable 손님 수 변경")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = createOrderTableWithNumberOfGuest(1);
        int expected = 7;

        OrderTable actual = orderTable.changeNumberOfGuests(expected);

        assertThat(actual.getNumberOfGuests()).isEqualTo(expected);
    }

    @DisplayName("OrderTable 비어있을 때 손님 수 변경 예외 발생")
    @Test
    void changeNumberOfGuestsFromEmptyTable() {
        OrderTable orderTable = createOrderTableWithEmpty(true);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.changeNumberOfGuests(777))
                .withMessage("비어있는 테이블은 손님 수를 변경할 수 없습니다.");
    }
}