package kitchenpos.application.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableServiceChangeGuestNumberTest extends TableServiceTest {

    @DisplayName("테이블의 손님 수 변경 시에 손님이 최소 1명보단 많아야 한다.")
    @Test
    void zeroGuest() {
        //given
        standardTable.setNumberOfGuests(0);

        //when

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, standardTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수 변경 시에 테이블이 존재해야 한다.")
    @Test
    void unexsitedTable() {
        //given
        given(orderTableDao.findById(1L)).willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, standardTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수 변경 시에 테이블이 비어있어선 안 된다.")
    @Test
    void emptyTable() {
        //given
        standardTable.setEmpty(true);
        given(orderTableDao.findById(1L)).willReturn(Optional.ofNullable(standardTable));

        //when

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, standardTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeTableNumberOfGuest() {
        //given
        standardTable.setEmpty(false);
        standardTable.setNumberOfGuests(5);
        given(orderTableDao.findById(1L)).willReturn(Optional.ofNullable(standardTable));
        given(orderTableDao.save(standardTable)).willReturn(standardTable);

        //when
        OrderTable orderTable = tableService.changeNumberOfGuests(1L, standardTable);

        //then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(5L);
    }

}
