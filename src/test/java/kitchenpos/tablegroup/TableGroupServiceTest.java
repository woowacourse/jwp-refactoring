package kitchenpos.tablegroup;

import kitchenpos.menu.Menu;
import kitchenpos.order.Order;
import kitchenpos.order.OrderService;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.TableService;
import kitchenpos.tablegroup.dto.OrderTableRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.양념치킨;
import static kitchenpos.fixture.OrderTableFixture.테이블1;
import static kitchenpos.fixture.OrderTableFixture.테이블2;
import static kitchenpos.fixture.OrderTableFixture.테이블9;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TableGroupServiceTest {

    @Autowired
    private TableService tableService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private TableGroupService tableGroupService;

    @Test
    void 등록시_두_테이블_이상이어야한다() {
        var tableGroup = new TableGroupRequest(List.of(
                new OrderTableRequest(테이블1().getId())
        ));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록시_기존_테이블이어야한다() {
        var unsaved = 테이블9();
        var tableGroup = new TableGroupRequest(List.of(
                new OrderTableRequest(테이블1().getId()),
                new OrderTableRequest(unsaved.getId())
        ));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록시_테이블들은_비어있어야한다() {
        var tableGroupWithNotEmpty = new TableGroupRequest(List.of(
                new OrderTableRequest(테이블1().getId()),
                new OrderTableRequest(fillTable(테이블2()).getId())
        ));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupWithNotEmpty))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록시_생성시각을_기록한다() {
        LocalDateTime startedTime = LocalDateTime.now();
        var tableGroup = new TableGroupRequest(List.of(
                new OrderTableRequest(테이블1().getId()),
                new OrderTableRequest(테이블2().getId())
        ));

        assertThat(tableGroupService.create(tableGroup).getCreatedDate()).isBetween(startedTime, LocalDateTime.now());
    }

    @Test
    void 등록시_테이블들을_이용중으로_바꾼다() {
        var tableGroup = new TableGroupRequest(List.of(
                new OrderTableRequest(테이블1().getId()),
                new OrderTableRequest(테이블2().getId())
        ));

        assertThat(tableGroupService.create(tableGroup).getOrderTables()).allMatch(it -> !it.isEmpty());
    }

    @Test
    void 등록한_테이블그룹을_반환한다() {
        var tableGroup = new TableGroupRequest(List.of(
                new OrderTableRequest(테이블1().getId()),
                new OrderTableRequest(테이블2().getId())
        ));

        assertThat(tableGroupService.create(tableGroup).getId()).isNotNull();
    }

    @Test
    void 그룹해제시_조리중이거나_식사중이면_안된다() {
        var tableGroup = new TableGroupRequest(List.of(
                new OrderTableRequest(테이블1().getId()),
                new OrderTableRequest(테이블2().getId())
        ));
        var grouped = tableGroupService.create(tableGroup);

        var item = new OrderLineItemRequest(양념치킨().getId(), 1);
        var order = new OrderRequest(grouped.getOrderTables().get(0).getId(), List.of(item));
        orderService.create(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(grouped.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 그룹해제시_테이블그룹을_제거한다() {
        var tableGroup = new TableGroupRequest(List.of(
                new OrderTableRequest(테이블1().getId()),
                new OrderTableRequest(테이블2().getId())
        ));

        var grouped = tableGroupService.create(tableGroup);
        tableGroupService.ungroup(grouped.getId());

        tableService.list().forEach(it -> assertThat(it.getTableGroupId()).isNull());
    }

    private OrderTable fillTable(OrderTable emptyTable) {
        return tableService.changeEmpty(emptyTable.getId(), false);
    }

    private Order orderOneFromTable1(Menu menu) {
        var fullTable = fillTable(테이블1());
        var item = new OrderLineItemRequest(menu.getId(), 1);
        var order = new OrderRequest(fullTable.getId(), List.of(item));

        return orderService.create(order);
    }
}
