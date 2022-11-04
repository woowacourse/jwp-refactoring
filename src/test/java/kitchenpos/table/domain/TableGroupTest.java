package kitchenpos.table.domain;

import static java.util.stream.Collectors.*;
import static kitchenpos.common.fixture.OrderTableFixtures.*;
import static kitchenpos.common.fixture.TableGroupFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.common.UnitTest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.Test;

@UnitTest
class TableGroupTest {

    @Test
    void tableGroup을_생성한다() {
        List<OrderTable> orderTables = List.of(
                generateOrderTable(1L, null, 0, true),
                generateOrderTable(2L, null, 0, true)
        );

        TableGroup actual = new TableGroup(orderTables);

        assertAll(() -> {
            assertThat(actual.getOrderTables()).hasSize(2);

            List<Boolean> empties = actual.getOrderTables()
                    .stream()
                    .map(OrderTable::isEmpty)
                    .collect(toList());
            assertThat(empties).containsExactly(false, false);
        });
    }

    @Test
    void tableGroup을_생성할_때_orderTables가_null인_경우_예외를_던진다() {
        assertThatThrownBy(() -> new TableGroup(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void tableGroup을_생성할_때_orderTables가_비어_있는_경우_예외를_던진다() {
        assertThatThrownBy(() -> new TableGroup(List.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void tableGroup을_ungroup한다() {
        List<OrderTable> orderTables = List.of(
                generateOrderTable(1L, null, 0, true),
                generateOrderTable(2L, null, 0, true)
        );

        TableGroup tableGroup = generateTableGroup(1L, LocalDateTime.now(), orderTables);

        tableGroup.ungroup(() -> false);

        assertAll(() -> {
                    List<Long> ids = orderTables.stream()
                            .map(OrderTable::getTableGroupId)
                            .collect(toList());
                    assertThat(ids).containsExactly(null, null);

                    List<Boolean> empties = orderTables.stream()
                            .map(OrderTable::isEmpty)
                            .collect(toList());
                    assertThat(empties).containsExactly(false, false);
                }
        );
    }

    @Test
    void tableGroup을_ungroup할_때_특정_조건을_만족하지_못하면_예외를_던진다() {
        TableGroup tableGroup = generateTableGroup(1L, LocalDateTime.now(), List.of());

        assertThatThrownBy(() -> tableGroup.ungroup(() -> true))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
