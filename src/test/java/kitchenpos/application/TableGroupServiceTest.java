package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTestContext {

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1})
    void 두개_이상의_테이블을_그룹으로_지정하지_않으면_예외를_던진다(int tableSize) {
        // given
        List<OrderTable> orderTables = new ArrayList<>();
        for (int i = 0; i < tableSize; i++) {
            orderTables.add(new OrderTable());
        }

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_테이블을_참조하면_예외를_던진다() {
        // given
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(new OrderTable());
        orderTables.add(new OrderTable());

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 그룹_지정_대상이_빈_테이블이_아니라면_예외를_던진다() {
        // given
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(savedOrderTable);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 그룹_지정_대상이_이미_그룹이_존재한다면_예외를_던진다() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        TableGroup createdTableGroup = tableGroupDao.save(tableGroup);

        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(createdTableGroup.getId());
        orderTableDao.save(orderTable);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(createdTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 정상적으로_그룹을_생성하면_생성한_그룹을_반환한다() {
        // given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        OrderTable createdOrderTable1 = orderTableDao.save(orderTable1);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        OrderTable createdOrderTable2 = orderTableDao.save(orderTable2);

        TableGroup tableGroupRequest = new TableGroup();
        tableGroupRequest.setOrderTables(List.of(
                createdOrderTable1,
                createdOrderTable2
        ));

        // when
        TableGroup createdTableGroup = tableGroupService.create(tableGroupRequest);

        // then
        assertThat(createdTableGroup.getId()).isNotNull();
    }

    @ParameterizedTest
    @EnumSource(mode = Mode.INCLUDE, names = {"COOKING", "MEAL"})
    void 주문_상태가_COOKING이거나_MEAL인_경우_그룹을_해체할_수_없다(OrderStatus orderStatus) {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        TableGroup createdTableGroup = tableGroupDao.save(tableGroup);

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setTableGroupId(createdTableGroup.getId());
        OrderTable createdOrderTable = orderTableDao.save(orderTable);

        Order order = new Order();
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(orderStatus.name());
        order.setOrderTableId(createdOrderTable.getId());
        orderDao.save(order);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(createdTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
