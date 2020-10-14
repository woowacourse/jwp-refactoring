package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("주문 등록 불가 여부 변경")
    @Test
    void changeEmpty() {
        OrderTable orderTable = OrderTable.builder()
            .empty(true)
            .build();

        orderTable.changeEmpty(false);

        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("[예외] 그룹에 포함된 테이블의 주문 등록 불가 여부 변경")
    @Test
    void changeEmpty_Fail_With_TableInGroup() {
        TableGroup tableGroup = TableGroup.builder()
            .createdDate(LocalDateTime.now())
            .build();

        OrderTable orderTable = OrderTable.builder()
            .empty(true)
            .tableGroup(tableGroup)
            .build();

        assertThatThrownBy(() -> orderTable.changeEmpty(false))
            .isInstanceOf(IllegalArgumentException.class);
    }
}