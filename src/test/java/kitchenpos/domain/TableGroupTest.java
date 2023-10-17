package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.exception.OrderTableCountNotEnoughException;
import kitchenpos.exception.OrderTableNotEmptyException;
import kitchenpos.exception.TableGroupExistsException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class TableGroupTest {

    @Test
    void 주문_테이블들을_그룹화하려_할_때_비지_않은_테이블이_있다면_예외를_던진다() {
        // given
        TableGroup tableGroup = new TableGroup(null);
        List<OrderTable> orderTables = List.of(new OrderTable(null, 1, true),
                new OrderTable(null, 1, false));

        // when, then
        assertThatThrownBy(() -> tableGroup.groupOrderTables(orderTables))
                .isInstanceOf(OrderTableNotEmptyException.class);
    }

    @Test
    void 주문_테이블들을_그룹화하려_할_때_테이블_그룹이_이미_있는_테이블이_있다면_예외를_던진다() {
        // given
        TableGroup tableGroup = new TableGroup(null);
        List<OrderTable> orderTables = List.of(new OrderTable(null, 1, true),
                new OrderTable(new TableGroup(null), 1, true));

        // when, then
        assertThatThrownBy(() -> tableGroup.groupOrderTables(orderTables))
                .isInstanceOf(TableGroupExistsException.class);
    }

    @Test
    void 주문_테이블들을_정상적으로_그룹화하려_할_때_주문_테이블_개수가_2개_미만이라면_예외를_던진다() {
        // given
        TableGroup tableGroup = new TableGroup(null);
        List<OrderTable> orderTables = List.of(new OrderTable(null, 1, true));

        // when, then
        assertThatThrownBy(() -> tableGroup.groupOrderTables(orderTables))
                .isInstanceOf(OrderTableCountNotEnoughException.class);
    }

    @Test
    void 주문_테이블들을_정상적으로_그룹화_할_수_있다() {
        // given
        TableGroup tableGroup = new TableGroup(null);
        List<OrderTable> orderTables = List.of(new OrderTable(null, 1, true),
                new OrderTable(null, 1, true));

        // when
        tableGroup.groupOrderTables(orderTables);

        // then
        assertThat(tableGroup.getOrderTables()).isEqualTo(orderTables);
    }

    @Test
    void 주문_테이블들을_그룹_해제_할_수_있다() {
        // given
        TableGroup tableGroup = new TableGroup(null);
        List<OrderTable> orderTables = List.of(new OrderTable(null, 1, true),
                new OrderTable(null, 1, true));
        tableGroup.groupOrderTables(orderTables);

        // when
        tableGroup.unGroupOrderTables();

        // then
        assertThat(tableGroup.getOrderTables())
                .filteredOn(each -> each.getTableGroup() != null)
                .hasSize(0);
    }
}
