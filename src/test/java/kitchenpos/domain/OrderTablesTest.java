package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

class OrderTablesTest {
    public static Stream<Arguments> invalidOrderTables() {
        return Stream.of(
                Arguments.of(Collections.emptyList()),
                Arguments.of(Collections.singletonList(new OrderTable(null, null, 0, true)))
        );
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("invalidOrderTables")
    void orderTable이_null이거나_사이즈가_2보다_작을_때_OrderTables를_생성할_수_없다(List<OrderTable> orderTables) {
        // when & then
        assertThatThrownBy(() -> new OrderTables(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void orderTable이_빈_상태가_아니라면_OrderTables를_생성할_수_없다() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(null, null, 0, false),
                new OrderTable(null, null, 0, true)
        );

        // when & then
        assertThatThrownBy(() -> new OrderTables(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void orderTable에_이미_tableGroup이_존재하면_OrderTables를_생성할_수_없다() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(null, 1L, 0, true),
                new OrderTable(null, null, 0, true)
        );

        // when & then
        assertThatThrownBy(() -> new OrderTables(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
