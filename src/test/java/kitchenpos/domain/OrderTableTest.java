package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    @DisplayName("그룹으로 묶여있다면 해당 테이블을 비울 수 없다.")
    void changeEmptyError(){
        //given
        OrderTable orderTable = new OrderTable(1, false);

        //when, then
        assertThatThrownBy(()-> orderTable.changeEmpty(true))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("그룹으로 묶인 테이블은 비울 수 없습니다.");
    }

    @Test
    @DisplayName("0 미만으로 손님 수를 수정할 수 없다.")
    void updateLessThanZeroError(){
        //given
        OrderTable orderTable = new OrderTable(1, false);

        //when, then
        assertThatThrownBy(()-> orderTable.updateNumberOfGuests(2))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("손님의 수는 0 미만일 수 없습니다.");
    }
}
