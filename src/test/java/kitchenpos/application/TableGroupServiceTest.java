package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@ApplicationTest
class TableGroupServiceTest {

    private final TableGroupService tableGroupService;
    private final TableService tableService;
    private final OrderService orderService;

    TableGroupServiceTest(TableGroupService tableGroupService, TableService tableService,
                          OrderService orderService) {
        this.tableGroupService = tableGroupService;
        this.tableService = tableService;
        this.orderService = orderService;
    }

    @Test
    void 테이블그룹을_생성한다() {
        List<OrderTable> orderTables = tableService.list();
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTables.get(0), orderTables.get(1)));

        assertThat(tableGroupService.create(tableGroup)).isInstanceOf(TableGroup.class);
    }

    @Test
    void 테이블그룹을_생성할때_테이블이_없는_경우_예외를_발생시킨다() {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), null);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_생성할때_테이블이_2개보다_적은_경우_예외를_발생시킨다() {
        List<OrderTable> orderTables = tableService.list();
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTables.get(0)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_생성할때_테이블이_저장된_정보와_다를_경우_예외를_발생시킨다() {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                List.of(new OrderTable(-1L, null, 0, true), new OrderTable(-2L, null, 0, true)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_생성할때_테이블이_비어있지_않은_경우_예외를_발생시킨다() {
        List<OrderTable> orderTables = tableService.list();
        OrderTable orderTable = orderTables.get(2);
        orderTable.setEmpty(false);
        tableService.changeEmpty(orderTable.getId(), orderTable);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable, orderTables.get(1)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_해제한다() {
        List<OrderTable> orderTables = tableService.list();
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTables.get(2), orderTables.get(3)));
        TableGroup savedGroup = tableGroupService.create(tableGroup);

        Assertions.assertDoesNotThrow(() -> tableGroupService.ungroup(savedGroup.getId()));
    }

    @Test
    void 테이블그룹을_해제할때_조리중이거나_식사중인_주문이_있는_경우_예외를_발생시킨다() {
        List<OrderTable> orderTables = tableService.list();
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTables.get(0), orderTables.get(1)));
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
