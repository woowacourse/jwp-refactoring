package kitchenpos.core.table.domain;

import static kitchenpos.fixture.TableFixture.getOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.core.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TableGroupTest {

    @Test
    @DisplayName("주문 테이블의 개수가 2개 미만이면 예외가 발생한다.")
    void createWithInvalidSize() {
        assertThatThrownBy(() -> TableGroup.of(1L, LocalDateTime.now(), Arrays.asList(getOrderTable())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 2개 이상이어야 합니다.");
    }

    @Test
    @DisplayName("주문 테이블이 없이 테이블 그룹을 생성하면 예외가 발생한다.")
    void createWithInvalidEmptyOrderTables() {
        assertThatThrownBy(() -> TableGroup.of(1L, LocalDateTime.now(), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블 없이 테이블 그룹을 생성할 수 없습니다.");
    }

    @Test
    @DisplayName("이미 테이블 그룹이 있는 주문 테이블로 테이블 그룹을 생성하면 예외가 발생한다.")
    void createWithTableGroupOrderTables() {
        // given
        final List<OrderTable> orderTables = Arrays.asList(getOrderTable(1L, true), getOrderTable(1L, true));
        assertThatThrownBy(() -> TableGroup.of(1L, LocalDateTime.now(), orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 테이블 그룹을 가진 주문 테이블은 테이블 그룹을 생성할 수 없습니다.");
    }

    @Test
    @DisplayName("비어있지 않은 주문 테이블로 테이블 그룹을 생성하면 예외가 발생한다.")
    void createWithNotEmptyOrderTables() {
        // given
        final List<OrderTable> orderTables = Arrays.asList(getOrderTable(null, false), getOrderTable(null, false));
        assertThatThrownBy(() -> TableGroup.of(1L, LocalDateTime.now(), orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 비어있지 않으면 테이블 그룹을 생성할 수 없습니다.");
    }

    @Test
    @DisplayName("테이블 그룹을 생성하면 테이블 그룹에 속한 테이블은 테이블 그룹 id를 가져야 한다.")
    void createThenOrderTableHaveTableGroupId() {
        final List<OrderTable> orderTables = Arrays.asList(getOrderTable(null, true), getOrderTable(null, true));
        final TableGroup tableGroup = TableGroup.of(1L, LocalDateTime.now(), orderTables);
        final List<OrderTable> actualOrderTables = tableGroup.getOrderTables();

        assertAll(
                () -> assertThat(actualOrderTables.get(0).getTableGroupId()).isEqualTo(1L),
                () -> assertThat(actualOrderTables.get(1).getTableGroupId()).isEqualTo(1L)
        );
    }

    @ParameterizedTest(name = "주문 테이블이 {0} 상태아면 테이블 그룹을 해제할 수 없다.")
    @ValueSource(strings = {"COOKING", "MEAL"})
    void ungroupWithInvalidOrderTableStatus(final String orderStatus) {
        // given
        final OrderTable orderTable1 = getOrderTable(null, true);
        final OrderTable orderTable2 = getOrderTable(null, true);
        final List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        final TableGroup tableGroup = TableGroup.of(1L, LocalDateTime.now(), orderTables);

        // when
        orderTable1.changeOrderStatus(OrderStatus.from(orderStatus));
        orderTable2.changeOrderStatus(OrderStatus.from(orderStatus));

        // then
        assertThatThrownBy(tableGroup::unGroup)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리중이거나 식사중이면 주문 테이블의 비운 상태를 수정할 수 없습니다.");
    }

    @Test
    @DisplayName("테이블 그룹을 해제하면 주문 테이블들은 비어있지 않아야 한다.")
    void ungroupThenOrderTableIsNotEmpty() {
        // given
        final OrderTable orderTable1 = getOrderTable(null, true, OrderStatus.COMPLETION);
        final OrderTable orderTable2 = getOrderTable(null, true, OrderStatus.COMPLETION);
        final List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        final TableGroup tableGroup = TableGroup.of(1L, LocalDateTime.now(), orderTables);

        // when
        tableGroup.unGroup();

        // then
        assertAll(
                () -> assertThat(orderTable1.isEmpty()).isFalse(),
                () -> assertThat(orderTable2.isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("테이블 그룹을 해제하면 주문 테이블들은 테이블 그룹 id를 갖지 않아야 한다.")
    void ungroupThenOrderTableDontHaveTableGroupId() {
        // given
        final OrderTable orderTable1 = getOrderTable(null, true, OrderStatus.COMPLETION);
        final OrderTable orderTable2 = getOrderTable(null, true, OrderStatus.COMPLETION);
        final List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        final TableGroup tableGroup = TableGroup.of(1L, LocalDateTime.now(), orderTables);

        // when
        tableGroup.unGroup();

        // then
        assertAll(
                () -> assertThat(orderTable1.getTableGroupId()).isNull(),
                () -> assertThat(orderTable2.getTableGroupId()).isNull()
        );
    }
}