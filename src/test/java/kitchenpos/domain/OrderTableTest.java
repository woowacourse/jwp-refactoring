package kitchenpos.domain;

import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @DisplayName("OrderTable 생성 실패 - 손님수가 0보다 작은 경우")
    @Test
    void create() {
        //given
        //when
        //then
        assertThatThrownBy(() -> new OrderTable(-1, false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 empty 변경 테스트")
    @Test
    void changeEmpty() {
        OrderTable fullTable = new OrderTable(3, false);
        OrderTable emptyTable = new OrderTable(0, true);

        fullTable.changeEmpty(emptyTable.isEmpty());

        assertThat(fullTable.isEmpty()).isEqualTo(emptyTable.isEmpty());
    }

    @DisplayName("테이블 empty 변경 테스트 - 실패 - 그룹에 포함된 테이블인 경우")
    @Test
    void changeEmptyFailureWhenTableInGroup() {
        TableGroup tableGroup = TableGroupFixture.create();

        OrderTable orderTable1 = new OrderTable(null, tableGroup, 3, false);

        assertThatThrownBy(() -> orderTable1.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("GuestNumber 변경")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable(3, false);
        OrderTable targetTable = new OrderTable(10, false);

        orderTable.changeNumberOfGuests(targetTable.getNumberOfGuests());

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(targetTable.getNumberOfGuests());
    }

    @DisplayName("GuestNumber 변경 - 실패 - 0보다 작은 수의 GuestNumber 인 경우")
    @Test
    void changeNumberOfGuestsFailureWhenInvalidNumberOfGuest() {
        OrderTable orderTable = new OrderTable(3, false);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("GuestNumber 변경 - 실패 - 빈 테이블인 경우")
    @Test
    void changeNumberOfGuestsFailureWhenEmptyTable() {
        OrderTable orderTable = new OrderTable(3, true);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(10))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
