package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    @DisplayName("변경 가능한 상태가 아니라면 변경할 수 없다.")
    void changeEmptyError(){
        //given

        //when

        //then
    }

    @Test
    @DisplayName("0 미만으로 손님 수를 수정할 수 없다.")
    void updateLessThanZeroError(){
        //given

        //when

        //then
    }
}