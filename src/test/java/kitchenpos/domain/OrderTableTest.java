package kitchenpos.domain;

import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1;
import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1_NUMBER_OF_GUESTS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.exception.OrderTableException;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    @DisplayName("그룹을 해제한다.")
    void unGroup() {
        // given
        TableGroup tableGroup = TableGroup.create(List.of(ORDER_TABLE1()), 1, 1);
        OrderTable orderTable = new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, true);
        orderTable.updateTableGroupId(tableGroup.getId());

        // when
        orderTable.unGroup();

        // then
        assertSoftly(softly -> {
            softly.assertThat(orderTable.getTableGroupId()).isNull();
            softly.assertThat(orderTable.isEmpty()).isFalse();
        });
    }

    @Test
    @DisplayName("손님 수 변경 시 테이블이 비어있으면 손님 수를 변경할 수 없으므로 예외가 발생한다.")
    void validateAvailableChangeNumberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, true);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(2))
                .isInstanceOf(OrderTableException.CannotChangeNumberOfGuestsStateInEmptyException.class)
                .hasMessage("[ERROR] 주문 테이블이 비어있는 상태에서 손님 수를 변경할 수 없습니다.");
    }
}
