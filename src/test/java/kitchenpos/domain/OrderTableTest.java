package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTableTest {

    @DisplayName("[SUCCESS] 입력 받은 주문 테이블로 주문 상태를 변경한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "false:false:false",
            "false:true:true",
            "true:false:false",
            "true:true:true",
    }, delimiter = ':')
    void changeOrderTableEmpty(final boolean original, final boolean actual, final boolean expected) {
        // given
        final OrderTable orderTable = new OrderTable(null, 5, original);

        // when
        final OrderTable status = new OrderTable(null, 5, actual);
        orderTable.changeEmpty(status);

        // then
        assertThat(orderTable.isEmpty()).isEqualTo(expected);
    }

    @DisplayName("[SUCCESS] 주문 테이블 상태를 비어있지 않음으로 변경한다.")
    void changeOrderTableFull() {
        // given
        final OrderTable orderTable = new OrderTable(null, 5, true);

        // when
        orderTable.changeOrderTableFull();

        // then
        assertThat(orderTable.isEmpty()).isFalse();
    }
}
