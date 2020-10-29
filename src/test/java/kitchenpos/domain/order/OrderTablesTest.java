package kitchenpos.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTablesTest {
    static Stream<Arguments> invalidOrderTablesSource() {
        return Stream.of(
                Arguments.of(Collections.emptyList()),
                Arguments.of(Collections.singletonList(
                        new OrderTable(1L, new TableGroup(), NumberOfGuests.of(1), Empty.of(true))))
        );
    }

    @DisplayName("잘못된 주문 테이블들로 생성시 예외 반환")
    @ParameterizedTest
    @MethodSource("invalidOrderTablesSource")
    void ofTest(List<OrderTable> input) {
        assertThatThrownBy(() -> {
            OrderTables.of(input);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹에 속하지 않거나 비어있지 않은 주문 테이블 입력 시 예외 출력")
    @Test
    void validateGroupTablesTest() {
        OrderTable nonGroupOrderTable = new OrderTable(1L, null, NumberOfGuests.of(10), Empty.of(true));
        OrderTable nonEmptyOrderTable = new OrderTable(1L, new TableGroup(), NumberOfGuests.of(10), Empty.of(false));
        OrderTables orderTables = OrderTables.of(Arrays.asList(nonGroupOrderTable, nonEmptyOrderTable));

        assertThatThrownBy(() -> {
            orderTables.validateGroupTables();
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("입력한 두 개의 주문 테이블의 개수가 다르면 예외 반환")
    @Test
    void isInvalidOrderTablesTest() {
        OrderTable firstOrderTable = new OrderTable(1L, new TableGroup(), NumberOfGuests.of(1), Empty.of(true));
        OrderTable secondOrderTable = new OrderTable(1L, new TableGroup(), NumberOfGuests.of(1), Empty.of(true));
        OrderTable thirdOrderTable = new OrderTable(1L, new TableGroup(), NumberOfGuests.of(1), Empty.of(true));
        OrderTables sizeThreeOrderTables = OrderTables.of(
                Arrays.asList(firstOrderTable, secondOrderTable, thirdOrderTable)
        );
        OrderTables sizeTwoOrderTables = OrderTables.of(
                Arrays.asList(firstOrderTable, secondOrderTable)
        );

        assertThatThrownBy(() -> {
            sizeThreeOrderTables.isInvalidOrderTables(sizeTwoOrderTables);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블에 포함된 주문 테이블의 아이디 반환")
    @Test
    void getOrderTableIdsTest() {
        OrderTable firstOrderTable = new OrderTable(1L, new TableGroup(), NumberOfGuests.of(1), Empty.of(true));
        OrderTable secondOrderTable = new OrderTable(2L, new TableGroup(), NumberOfGuests.of(1), Empty.of(true));
        OrderTable thirdOrderTable = new OrderTable(3L, new TableGroup(), NumberOfGuests.of(1), Empty.of(true));
        OrderTables orderTables = OrderTables.of(
                Arrays.asList(firstOrderTable, secondOrderTable, thirdOrderTable)
        );

        List<Long> orderTableIds = orderTables.getOrderTableIds();

        assertAll(
                () -> assertThat(orderTableIds).hasSize(3),
                () -> assertThat(orderTableIds).containsExactly(1L, 2L, 3L)
        );
    }
}