package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("OrderTable 은 ")
class OrderTableTest {

    @DisplayName("테이블 그룹에 속한 주문 테이블 인지를 판별한다.")
    @Test
    void isPartOfTableGroup() {
        final OrderTable orderTable = new OrderTable(0, true);

        assertThat(orderTable.isPartOfTableGroup()).isFalse();
    }
}
