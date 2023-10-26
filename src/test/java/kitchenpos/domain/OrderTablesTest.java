package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTablesTest {

    @DisplayName("비어 있는 테이블 그룹을 생성하려는 경우 예외가 발생한다.")
    @Test
    void create_failEmptyTableGroup() {
        // given
        // when
        // then
        assertThatThrownBy(() -> new TableGroup(1L, List.of()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("2개 미만의 테이블로 구성된 테이블 그룹을 생성하려는 경우 예외가 발생한다.")
    @Test
    void create_failLessThanTwoTableGroup() {
        // given
        // when
        // then
        assertThatThrownBy(() -> new TableGroup(1L, List.of(new OrderTable())))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어 있지 않은 테이블이 포함된 테이블 그룹을 생성하려는 경우 예외가 발생한다.")
    @Test
    void registerTableGroup_failNotEmptyTable() {
        // given
        final OrderTable orderTable1 = new OrderTable(1L, 1, false, List.of(new Order(), new Order()));
        final OrderTable orderTable2 = new OrderTable(2L, 1, true, List.of(new Order()));
        final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));

        // when

        // then
        assertThatThrownBy(() -> orderTables.registerTableGroup(new TableGroup(1L, List.of(orderTable1, orderTable2))))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        // given
        final OrderTable orderTable1 = new OrderTable(1L, 1, true, List.of(
            new Order(1L, OrderStatus.COMPLETION, List.of(new OrderLineItem(1L, 1L, "치킨", new MenuPrice(BigDecimal.TEN), null)))
        ));
        final OrderTable orderTable2 = new OrderTable(2L, 1, true, List.of(
            new Order(2L, OrderStatus.COMPLETION, List.of(new OrderLineItem(1L, 1L, "치킨", new MenuPrice(BigDecimal.TEN), null)))
        ));
        final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));

        // when
        orderTables.ungroup();

        // then
        assertThat(orderTable1.hasTableGroup()).isFalse();
        assertThat(orderTable2.hasTableGroup()).isFalse();
    }

    @DisplayName("주문이 완료되지 않은 테이블의 테이블 그룹을 해제하려고 하는 경우 예외가 발생한다.")
    @Test
    void ungroup_failNotCompletionOrder() {
        // given
        final OrderTable orderTable1 = new OrderTable(1L, 1, true, List.of(
            new Order(1L, OrderStatus.COOKING, List.of(new OrderLineItem(1L, 1L, "치킨", new MenuPrice(BigDecimal.TEN), null)))
        ));
        final OrderTable orderTable2 = new OrderTable(2L, 1, true, List.of(
            new Order(2L, OrderStatus.COMPLETION, List.of(new OrderLineItem(1L, 1L, "치킨", new MenuPrice(BigDecimal.TEN), null)))
        ));
        final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));

        // when
        // then
        assertThatThrownBy(orderTables::ungroup)
            .isInstanceOf(IllegalArgumentException.class);
    }
}
