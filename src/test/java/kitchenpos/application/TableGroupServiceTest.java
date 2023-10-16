package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.fake.FakeOrderDao;
import kitchenpos.fake.FakeOrderTableDao;
import kitchenpos.fake.FakeTableGroupDao;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class TableGroupServiceTest {

    private OrderDao orderDao = new FakeOrderDao();
    private OrderTableDao orderTableDao = new FakeOrderTableDao();
    private TableGroupDao tableGroupDao = new FakeTableGroupDao();

    private TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

    @Test
    void 테이블_그룹을_생성한다() {
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(1L, 3, true));
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(2L, 4, true));

        TableGroup tableGroup = new TableGroup(null, null, of(orderTable1, orderTable2));
        TableGroup saved = tableGroupService.create(tableGroup);

        assertThat(tableGroup).usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @Test
    void 두개_미만의_테이블로_그룹할_수_없다() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(1L, 3, true));

        TableGroup tableGroup = new TableGroup(null, null, of(orderTable));
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_테이블로_그룹할_수_없다() {
        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(null, null, of(
                new OrderTable(1L, 3, true),
                new OrderTable(2L, 4, true)
        ))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_비어있지_않으면_그룹할_수_없다() {
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(1L, 3, false));
        OrderTable notEmptyTable = orderTableDao.save(new OrderTable(2L, 4, true));

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(null, null, of(orderTable1, notEmptyTable))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 그룹이_있다면_그룹할_수_없다() {
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(1L, 3, true));
        OrderTable groupedTable = orderTableDao.save(new OrderTable(2L, 3, true));
        groupedTable.setTableGroupId(1L);

        assertThatThrownBy(() -> tableGroupService.create(new TableGroup(null, null, of(orderTable1, groupedTable))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹이_생성되면서_테이블을_비어있지_않은_상태로_만든다() {
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(1L, 3, true));
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(2L, 4, true));

        TableGroup tableGroup = new TableGroup(null, null, of(orderTable1, orderTable2));
        TableGroup saved = tableGroupService.create(tableGroup);

        assertThat(saved.getOrderTables()).map(OrderTable::isEmpty)
                .containsExactly(false, false);
    }

    @Test
    void 그룹_해제를_한다() {
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(1L, 3, true));
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(2L, 4, true));

        TableGroup tableGroup = new TableGroup(null, null, of(orderTable1, orderTable2));
        TableGroup saved = tableGroupService.create(tableGroup);

        tableGroupService.ungroup(saved.getId());

        assertAll(
                () -> assertThat(orderTableDao.findById(orderTable1.getId())).map(OrderTable::getTableGroupId)
                        .isEmpty(),
                () -> assertThat(orderTableDao.findById(orderTable2.getId())).map(OrderTable::getTableGroupId)
                        .isEmpty()
        );
    }

    @Test
    void 주문_상태가_요리중이라면_그룹_해제_할_수_없다() {
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(1L, 3, true));
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(2L, 4, true));

        TableGroup tableGroup = new TableGroup(null, null, of(orderTable1, orderTable2));
        TableGroup saved = tableGroupService.create(tableGroup);

        OrderLineItem orderLineItem = new OrderLineItem(null, 1L, 1L, 1L);
        Order cookingOrder = new Order(null, orderTable1, List.of(orderLineItem));
        cookingOrder.changeOrderStatus(OrderStatus.COOKING);
        orderDao.save(cookingOrder);

        assertThatThrownBy(() -> tableGroupService.ungroup(saved.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_상태가_식사중이라면_그룹_해제_할_수_없다() {
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(1L, 3, true));
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(2L, 4, true));
        TableGroup tableGroup = new TableGroup(null, null, of(orderTable1, orderTable2));
        TableGroup saved = tableGroupService.create(tableGroup);

        OrderLineItem orderLineItem = new OrderLineItem(null, 1L, 1L, 1L);
        Order mealOrder = new Order(null, orderTable1, List.of(orderLineItem));
        mealOrder.changeOrderStatus(OrderStatus.MEAL);
        orderDao.save(mealOrder);

        assertThatThrownBy(() -> tableGroupService.ungroup(saved.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
