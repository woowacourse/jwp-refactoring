package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTableTest {

    @Test
    @DisplayName("아이디를 설정한다")
    void setId(){
        // given
        OrderTable orderTable = new OrderTable();
        Long id = 999L;

        // when
        orderTable.setId(id);

        // then
        assertThat(orderTable.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("테이블 그룹 아이디를 설정한다")
    void setTableGroupId(){
        // given
        OrderTable orderTable = new OrderTable();
        Long tableGroupId = 999L;

        // when
        orderTable.setTableGroupId(tableGroupId);

        // then
        assertThat(orderTable.getTableGroupId()).isEqualTo(tableGroupId);
    }

    @Test
    @DisplayName("손님 숫자를 설정한다")
    void setNumberOfGuests(){
        // given
        OrderTable orderTable = new OrderTable();
        int numberOfGuests = 999;

        // when
        orderTable.setNumberOfGuests(numberOfGuests);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    @DisplayName("빈 테이블 여부를 설정한다")
    void setEmpty(){
        // given
        OrderTable orderTable = new OrderTable();
        boolean empty = true;

        // when
        orderTable.setEmpty(empty);

        // then
        assertThat(orderTable.isEmpty()).isEqualTo(empty);
    }
}
