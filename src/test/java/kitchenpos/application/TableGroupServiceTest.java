package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
        var tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(테이블1()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록시_기존_테이블이어야한다() {
        var tableGroup = new TableGroup();
        var unsaved = 테이블9();
        tableGroup.setOrderTables(List.of(테이블1(), unsaved));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록시_테이블들은_비어있어야한다() {
        var tableGroupWithNotEmpty = new TableGroup();
        tableGroupWithNotEmpty.setOrderTables(List.of(테이블1(), fillTable(테이블2())));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupWithNotEmpty))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록시_생성시각을_기록한다() {
        LocalDateTime startedTime = LocalDateTime.now();
        var tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(테이블1(), 테이블2()));

        assertThat(tableGroupService.create(tableGroup).getCreatedDate()).isBetween(startedTime, LocalDateTime.now());
    }

    @Test
    void 등록시_테이블들을_이용중으로_바꾼다() {
        var tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(테이블1(), 테이블2()));

        assertThat(tableGroupService.create(tableGroup).getOrderTables()).allMatch(it -> !it.isEmpty());
    }

    @Test
    void 등록한_테이블그룹을_반환한다() {
        var tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(테이블1(), 테이블2()));

        assertThat(tableGroupService.create(tableGroup).getId()).isNotNull();
    }

    @Test
    void 그룹해제시_조리중이거나_식사중이면_안된다() {
        var tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(테이블1(), 테이블2()));
        var saved = tableGroupService.create(tableGroup);

        orderFromTable1(양념치킨());

        assertThatThrownBy(() -> tableGroupService.ungroup(saved.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 그룹해제시_테이블그룹을_제거한다() {
        var tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(테이블1(), 테이블2()));

        var groupped = tableGroupService.create(tableGroup);
        tableGroupService.ungroup(groupped.getId());

        tableService.list().forEach(it -> assertThat(it.getTableGroupId()).isNull());
    }

    private OrderTable fillTable(OrderTable emptyTable) {
        emptyTable.setEmpty(false);
        return tableService.changeEmpty(emptyTable.getId(), emptyTable);
    }

    private Order orderFromTable1(Menu menu) {
        var fullTable = 테이블1();
        fullTable.setEmpty(false);
        try {
            tableService.changeEmpty(fullTable.getId(), fullTable);
        } catch (IllegalArgumentException ignored) {

        }
        var order = new Order();
        order.setOrderTableId(fullTable.getId());
        var item = new OrderLineItem();
        item.setMenuId(menu.getId());
        order.setOrderLineItems(List.of(item));

        return orderService.create(order);
    }
}
