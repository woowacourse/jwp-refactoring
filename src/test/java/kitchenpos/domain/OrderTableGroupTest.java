package kitchenpos.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTableGroupTest {

    @DisplayName("주문 테이블의 개수가 2개 미만이면 예외를 발생시킨다.")
    @Test
    void numberOfOrderTablesLessThanTwo_exception() {
        // then
        assertThatThrownBy(() -> new OrderTableGroup(LocalDateTime.now(), List.of(
                new OrderTable(2, false)
        ))).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("중복된 주문 테이블이 있으면 예외를 발생시킨다.")
    @Test
    void distinctOrderTable_exception() {
        // then
        assertThatThrownBy(() -> new OrderTableGroup(LocalDateTime.now(), List.of(
                new OrderTable(1L, 1L, 2, false),
                new OrderTable(1L, 1L, 2, false)
        ))).isInstanceOf(IllegalArgumentException.class);
    }
}
