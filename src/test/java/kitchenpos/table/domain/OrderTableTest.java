package kitchenpos.table.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.table.domain.NumberOfGuests.DEFAULT_NUMBER_OF_GUESTS;

class OrderTableTest {

    @Test
    @DisplayName("updateNumberOfGuests()를 호출하면 주문 테이블의 손님 수를 변경할 수 있다.")
    void updateNumberOfGuests() {
        //given
        final NumberOfGuests before = DEFAULT_NUMBER_OF_GUESTS;
        final NumberOfGuests after = new NumberOfGuests(4);
        final OrderTable orderTable = new OrderTable(before, true);

        //when
        orderTable.updateNumberOfGuests(after);

        //then
        Assertions.assertThat(orderTable.getNumberOfGuests()).isEqualTo(after);
    }

    @Test
    @DisplayName("updateEmpty()를 호출하면 주문 테이블이 비어있는 지 여부를 변경할 수 있다.")
    void updateEmpty() {
        //given
        final boolean before = true;
        final boolean after = false;
        final OrderTable orderTable = new OrderTable(DEFAULT_NUMBER_OF_GUESTS, before);

        //when
        orderTable.updateEmpty(after);

        //then
        Assertions.assertThat(orderTable.isEmpty()).isEqualTo(after);
    }
}