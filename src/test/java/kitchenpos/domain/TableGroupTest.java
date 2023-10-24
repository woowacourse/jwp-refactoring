package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class TableGroupTest {

    @DisplayName("그룹에 주문 테이블을 추가한다.")
    @Test
    void addOrderTables() {
        // given
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        final OrderTable orderTable1 = new OrderTable(3, true);
        final OrderTable orderTable2 = new OrderTable(4, true);

        // when, then
        tableGroup.addOrderTables(List.of(orderTable1, orderTable2));

        // then
        assertThat(tableGroup.getOrderTables()).hasSize(2);
    }

    @DisplayName("그룹에 주문 테이블을 추가할 때, 테이블이 2개보다 적다면 예외가 발생한다.")
    @ParameterizedTest
    @MethodSource("createWrongSizeOrderTables")
    void addOrderTables_wrongSize_fail(final List<OrderTable> orderTables) {
        // given
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        // when, then
        assertThatThrownBy(() -> tableGroup.addOrderTables(orderTables))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("그룹으로 묶을 테이블은 2개 이상이어야 합니다.");
    }

    private static Stream<List<OrderTable>> createWrongSizeOrderTables() {
        return Stream.of(
            List.of(new OrderTable(3, true)),
            Collections.emptyList()
        );
    }

    @DisplayName("그룹에 주문 테이블을 추가할 때, 비어있는 테이블이 있다면 예외가 발생한다.")
    @Test
    void addOrderTables_emptyTable_fail() {
        // given
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        final OrderTable orderTable1 = new OrderTable(3, true);
        final OrderTable orderTable2 = new OrderTable(4, false);

        // when, then
        assertThatThrownBy(() -> tableGroup.addOrderTables(List.of(orderTable1, orderTable2)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("그룹으로 묶을 수 없는 테이블입니다.");
    }

    @DisplayName("그룹에 주문 테이블을 추가할 때, 다른 그룹에 속한 테이블이 있다면 예외가 발생한다.")
    @Test
    void addOrderTables_alreadyInGroup_fail() {
        // given
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        final OrderTable orderTable1 = new OrderTable(3, true);
        final OrderTable orderTable2 = new OrderTable(4, true);

        orderTable1.groupBy(new TableGroup(LocalDateTime.now()));

        // when, then
        assertThatThrownBy(() -> tableGroup.addOrderTables(List.of(orderTable1, orderTable2)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("그룹으로 묶을 수 없는 테이블입니다.");
    }
}
