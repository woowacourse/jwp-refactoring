package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Stream;
import kitchenpos.domain.group.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class OrderTablesTest {

    @ParameterizedTest
    @MethodSource("orderTableCollection")
    @DisplayName("테이블이 비어있거나 2개 미만일 경우 예외가 발생한다.")
    void validateOrderTables(final List<OrderTable> orderTableCollection) {
        assertThatThrownBy(() -> new OrderTables(orderTableCollection))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 비어있거나 2개 미만일 수 없습니다.");
    }

    @ParameterizedTest
    @MethodSource("orderTables")
    @DisplayName("활성화되어 있거나 단체 지정된 주문 테이블은 그룹으로 만들 경우 예외가 발생한다.")
    void validateGroup(final OrderTables orderTables) {
        assertThatThrownBy(orderTables::validateGroup)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 활성화되어 있거나 이미 단체 지정되어 있을 수 없습니다.");
    }

    private static Stream<Arguments> orderTableCollection() {
        return Stream.of(
                Arguments.of(List.of()),
                Arguments.of(List.of(new OrderTable()))
        );
    }

    private static Stream<Arguments> orderTables() {
        OrderTable notEmptyOrderTable = OrderTable.builder()
                .empty(false)
                .build();
        OrderTable groupedOrderTable = OrderTable.builder()
                .empty(true)
                .build();
        OrderTable orderTable = OrderTable.builder()
                .empty(true)
                .build();
        TableGroup tableGroup = TableGroup.builder()
                .orderTables(new OrderTables(List.of(groupedOrderTable, orderTable)))
                .build();
        groupedOrderTable.arrangeGroup(tableGroup);
        return Stream.of(
                Arguments.of(new OrderTables(List.of(notEmptyOrderTable, orderTable))),
                Arguments.of(new OrderTables(List.of(notEmptyOrderTable, orderTable)))
        );
    }
}
