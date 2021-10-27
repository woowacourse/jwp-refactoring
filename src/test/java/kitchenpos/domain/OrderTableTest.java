package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTableTest {

    @DisplayName("OrderTable 빌더로 OrderTable을 생성할 수 있다")
    @Test
    void createOrderTableWithBuilder() {
        OrderTable orderTable = new OrderTable.Builder()
                .id(1L)
                .tableGroupId(2L)
                .numberOfGuests(300)
                .empty(true)
                .build();

        assertThat(orderTable.getId()).isEqualTo(1L);
        assertThat(orderTable.getTableGroupId()).isEqualTo(2L);
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(300);
        assertThat(orderTable.isEmpty()).isTrue();
    }

}
