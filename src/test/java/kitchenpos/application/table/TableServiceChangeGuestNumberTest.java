package kitchenpos.application.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableServiceChangeGuestNumberTest extends TableServiceTest {

    private static final Boolean CHANGED_EMPTY_STATE = false;
    private static final Integer ZERO_GUEST_NUMBER = 0;
    private static final Integer CHANGED_GUEST_NUMBER = 5;

    @DisplayName("테이블의 손님 수 변경 시에 손님이 최소 1명보단 많아야 한다.")
    @Test
    void zeroGuest() {
        //given
        standardTable.setNumberOfGuests(ZERO_GUEST_NUMBER);

        //when

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(BASIC_ORDER_TABLE_ID, standardTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수 변경 시에 테이블이 존재해야 한다.")
    @Test
    void unexsitedTable() {
        //given
        given(orderTableDao.findById(BASIC_ORDER_TABLE_ID)).willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(BASIC_ORDER_TABLE_ID, standardTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수 변경 시에 테이블이 비어있어선 안 된다.")
    @Test
    void emptyTable() {
        //given
        standardTable.setEmpty(BASIC_EMPTY_STATE);
        given(orderTableDao.findById(BASIC_ORDER_TABLE_ID)).willReturn(Optional.ofNullable(standardTable));

        //when

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(BASIC_ORDER_TABLE_ID, standardTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeTableNumberOfGuest() {
        //given
        standardTable.setEmpty(CHANGED_EMPTY_STATE);
        standardTable.setNumberOfGuests(CHANGED_GUEST_NUMBER);
        given(orderTableDao.findById(BASIC_ORDER_TABLE_ID)).willReturn(Optional.ofNullable(standardTable));
        given(orderTableDao.save(standardTable)).willReturn(standardTable);

        //when
        OrderTable orderTable = tableService.changeNumberOfGuests(BASIC_ORDER_TABLE_ID, standardTable);

        //then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(CHANGED_GUEST_NUMBER);
    }

}
