package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Stream;
import kitchenpos.domain.group.TableGroup;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.OrderTables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class OrderTablesTest {

    @ParameterizedTest
    @MethodSource("orderTables")
    @DisplayName("테이블이 비어있거나 2개 미만일 경우 예외가 발생한다.")
    void validateOrderTables(final List<OrderTable> orderTables) {
        assertThatThrownBy(() -> new OrderTables(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 비어있거나 2개 미만일 수 없습니다.");
    }

    @ParameterizedTest
    @MethodSource("orderTable")
    @DisplayName("활성화되어 있거나 단체 지정된 주문 테이블은 그룹으로 만들 경우 예외가 발생한다.")
    void validateGroup(final OrderTable orderTable) {
        assertThatThrownBy(() -> new OrderTables(List.of(orderTable)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 비어있거나 2개 미만일 수 없습니다.");
    }

    private static Stream<Arguments> orderTables() {
        return Stream.of(
                Arguments.of(List.of()),
                Arguments.of(List.of(new OrderTable()))
        );
    }

    private static Stream<Arguments> orderTable() {
        return Stream.of(
                Arguments.of(OrderTable.builder()
                        .empty(false)
                        .build()),
                Arguments.of(OrderTable.builder()
                        .tableGroup(new TableGroup())
                        .build())
        );
    }
}
