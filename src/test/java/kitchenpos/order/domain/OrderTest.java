package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.table.domain.OrderTable;

public class OrderTest {

    @Test
    @DisplayName("주문 완료 상태에서 상태를 변경하려고하면 에러를 발생시킨다.")
    void updateStatusInCompletionError(){
        //given
        Order order = new Order(new OrderTable(10, true), OrderStatus.COMPLETION, LocalDateTime.now(), Arrays.asList());

        //when, then
        assertThatThrownBy(()-> order.updateStatus(OrderStatus.MEAL))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주문이 완료되어 상태를 변경할 수 없습니다.");
    }
}
