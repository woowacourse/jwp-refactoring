package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderTableTest {

    @ParameterizedTest
    @ValueSource(ints = {-100, -1000, -100000})
    void constructor_인원이_음수일_경우_에러를_반환한다(int number) {
        assertThatThrownBy(() -> new OrderTable(number, false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 아이디가 null이 아닌지 확인한다.")
    @ParameterizedTest
    @ValueSource(longs = {1L, 50L, 100L})
    void validateTableGroupIdNotNull(Long groupId) {
        OrderTable orderTable = new OrderTable(1L, groupId, 1, true);
        assertThatThrownBy(orderTable::validateTableGroupIdNotNull)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
