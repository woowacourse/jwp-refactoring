package kitchenpos.table.domain;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static kitchenpos.support.fixture.TableFixture.비어있는_주문_테이블;
import static kitchenpos.table.domain.exception.TableGroupExceptionType.ORDER_TABLE_IS_NOT_EMPTY_OR_ALREADY_GROUPED;
import static kitchenpos.table.domain.exception.TableGroupExceptionType.ORDER_TABLE_SIZE_IS_LOWER_THAN_ZERO_OR_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import kitchenpos.table.domain.exception.TableGroupException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TableGroupTest {

    @Test
    @DisplayName("tableGroup을 생성할 때 orderTable의 사이즈가 2보다 작거나, 비어있으면 예외처리한다.")
    void throwExceptionOrderTableSizeIsSmallerThanZero() {
        assertThatThrownBy(() -> new TableGroup(now(), emptyList()))
            .isInstanceOf(TableGroupException.class)
            .hasMessage(ORDER_TABLE_SIZE_IS_LOWER_THAN_ZERO_OR_EMPTY.getMessage());
    }

    @Test
    @DisplayName("파라미터로 넘어온 orderTable이 비어있지 않다면 예외처리한다.")
    void throwExceptionOrderTableIsNotEmpty() {
        final OrderTable validTable = new OrderTable(5, true);
        final OrderTable invalidTable = new OrderTable(5, false);

        assertThatThrownBy(() -> new TableGroup(now(), List.of(invalidTable, validTable)))
            .isInstanceOf(TableGroupException.class)
            .hasMessage(ORDER_TABLE_IS_NOT_EMPTY_OR_ALREADY_GROUPED.getMessage());
    }

    @Test
    @DisplayName("tableGroup을 생성할 때 다른 TableGroup에 속해있으면 예외처리한다.")
    void throwExceptionOrderTableIsAlreadyGrouped() {
        final TableGroup mockTableGroup = Mockito.mock(TableGroup.class);
        final OrderTable invalidTable = new OrderTable(mockTableGroup, 5, true);
        final OrderTable validTable = new OrderTable(5, true);

        assertThatThrownBy(() -> new TableGroup(now(), List.of(invalidTable, validTable)))
            .isInstanceOf(TableGroupException.class)
            .hasMessage(ORDER_TABLE_IS_NOT_EMPTY_OR_ALREADY_GROUPED.getMessage());
    }

    @Test
    @DisplayName("table Group에 속해 있는 table들의 값을 변경한다.")
    void ungroupTable() {
        final OrderTable validTable1 = 비어있는_주문_테이블();
        final OrderTable validTable2 = 비어있는_주문_테이블();
        final TableGroup tableGroup = new TableGroup(now(), List.of(validTable1, validTable2));

        tableGroup.ungroup();

        assertAll(
            () -> assertThat(tableGroup.getOrderTables())
                .isEmpty(),
            () -> assertFalse(validTable1.isEmpty()),
            () -> assertFalse(validTable2.isEmpty())
        );
    }
}
