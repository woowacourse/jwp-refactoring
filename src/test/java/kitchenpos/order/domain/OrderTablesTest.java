package kitchenpos.order.domain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTablesTest {

    @DisplayName("OrderTables의 크기가 2미만이거나 0이면 예외를 발생한다.")
    @MethodSource("getShortOrderTables")
    @ParameterizedTest
    void validate_orderTables_size(final List<OrderTable> orderTables) {
        // given
        // when
        // then
        assertThatThrownBy(() -> new OrderTables(orderTables))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("테이블 그룹할 주문 테이블은 2개 이상이어야 합니다.");
    }

    private static Stream<Arguments> getShortOrderTables() {
        return Stream.of(
            Arguments.of(Collections.emptyList()),
            Arguments.of(List.of(new OrderTable(4)))
        );
    }

    @DisplayName("OrderTables에 비어있지 않거나 이미 다른 테이블 그룹에 포함된 경우 예외를 발생한다.")
    @MethodSource("getWrongOrderTables")
    @ParameterizedTest
    void validate_orderTables_is_not_empty_or_already_contained(final List<OrderTable> orderTables) {
        // given
        // when
        // then
        assertThatThrownBy(() -> new OrderTables(orderTables))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("테이블이 비어있지 않거나 이미 다른 그룹에 포함된 주문 테이블은 새로운 테이블 그룹에 속할 수 없습니다.");
    }

    private static Stream<Arguments> getWrongOrderTables() {
        return Stream.of(
            Arguments.of(List.of(
                new OrderTable(2, false),
                new OrderTable(3, true)
            )),
            Arguments.of(List.of(
                new OrderTable(1L, new TableGroup(2L), 4, false),
                new OrderTable(3, true)
            ))
        );
    }
}
