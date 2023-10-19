package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @DisplayName("[SUCCESS] 주문 테이블이 빈 상태를 변경한다.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void success_changeOrderTableFull(final boolean expected) {
        // given
        final OrderTable orderTable = new OrderTable(null, 5, true);

        // when
        orderTable.changeOrderTableEmpty(expected);

        // then
        assertThat(orderTable.isEmpty()).isEqualTo(expected);
    }

    @DisplayName("[EXCEPTION] 단체 지정이 되어있을 경우 주문 테이블을 비어있는 상태로 변경할 수 없다.")
    @Test
    void throwException_changeOrderTableEmpty_when_tableGroupIsNotNull() {
        // given
        final OrderTable orderTable = new OrderTable(
                new TableGroup(LocalDateTime.now(), new ArrayList<>()),
                5,
                false
        );

        // expect
        assertThatThrownBy(() -> orderTable.changeOrderTableEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
