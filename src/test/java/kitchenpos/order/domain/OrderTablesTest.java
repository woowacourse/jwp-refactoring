package kitchenpos.order.domain;

import static kitchenpos.util.ObjectUtil.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;

class OrderTablesTest {
    @DisplayName("OrderTables를 정상 생성한다.")
    @Test
    void createTest() {
        OrderTable firstOrderTable = createOrderTable(1L, 1L, 2, false);
        OrderTable secondOrderTable = createOrderTable(2L, 1L, 2, false);
        assertDoesNotThrow(() -> new OrderTables(Arrays.asList(firstOrderTable, secondOrderTable)));
    }

    @DisplayName("OrderTables 생성시 list가 비어있거나 size가 2미만인 경우 예외를 반환한다.")
    @ParameterizedTest
    @MethodSource("provideOrderTables")
    void createTest2(List<OrderTable> orderTables) {
        assertThatThrownBy(() -> new OrderTables(orderTables))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<List<OrderTable>> provideOrderTables() {
        OrderTable firstOrderTable = createOrderTable(1L, 1L, 2, false);
        return Stream.of(
            Collections.emptyList(),
            Collections.singletonList(firstOrderTable)
        );
    }
}
