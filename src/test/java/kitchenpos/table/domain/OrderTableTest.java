package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTableTest {

    @DisplayName("단체지정되어있는 테이블의 비어있는지 여부를 수정하려고하면 에러를 반환한다.")
    @Test
    void changeEmpty_fail_if_tableGroupId_not_null() {
        OrderTable orderTable = OrderTable.builder()
                .tableGroupId(1L)
                .build();

        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
