package kitchenpos.application.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableServiceCreateTest extends TableServiceTest {

    @DisplayName("테이블을 추가한다.")
    @Test
    void createTable() {
        //given
        given(orderTableDao.save(standardTable)).willReturn(standardTable);

        //when
        OrderTable orderTable = tableService.create(standardTable);

        //then
        assertAll(
            () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(BASIC_GUEST_NUMBER),
            () -> assertThat(orderTable.isEmpty()).isTrue()
        );
    }

}
