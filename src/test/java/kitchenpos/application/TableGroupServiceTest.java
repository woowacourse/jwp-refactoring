package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

import java.util.ArrayList;
import java.util.Arrays;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Test
    void 테이블을_단체로_지정할_수_있다() {
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(null, 1, true));
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(null, 2, true));

        TableGroup tableGroup = new TableGroup(new ArrayList<>(Arrays.asList(orderTable1, orderTable2)));

        TableGroup actual = tableGroupService.create(tableGroup);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getOrderTables()).hasSize(2);
        });
    }

    @Test
    void 단체로_지정할_테이블이_한_개_이하인_경우_지정할_수_없다() {
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(null, 1, true));

        TableGroup tableGroup = new TableGroup(new ArrayList<>(Arrays.asList(orderTable1)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체로_지정할_테이블_중_빈_테이블이_존재하는_경우_지정할_수_없다() {
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(null, 1, false));
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(null, 2, true));

        TableGroup tableGroup = new TableGroup(new ArrayList<>(Arrays.asList(orderTable1, orderTable2)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체로_지정할_테이블_중_이미_단체로_지정된_테이블이_존재하는_경우_지정할_수_없다() {
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(null, 1, true));
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(null, 2, true));

        TableGroup tableGroup1 = tableGroupService.create(
                new TableGroup(new ArrayList<>(Arrays.asList(orderTable1, orderTable2))));

        OrderTable orderTable3 = orderTableDao.save(new OrderTable(tableGroup1.getId(), 1, true));
        OrderTable orderTable4 = orderTableDao.save(new OrderTable(null, 1, true));

        TableGroup tableGroup2 = new TableGroup(new ArrayList<>(Arrays.asList(orderTable3, orderTable4)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup2)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_단체_지정을_취소할_수_있다() {
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(null, 1, true));
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(null, 2, true));

        TableGroup tableGroup = tableGroupService.create(
                new TableGroup(new ArrayList<>(Arrays.asList(orderTable1, orderTable2))));

        tableGroupService.ungroup(tableGroup.getId());

        OrderTable foundOrderTable1 = orderTableDao.findById(orderTable1.getId()).get();
        OrderTable foundOrderTable2 = orderTableDao.findById(orderTable2.getId()).get();

        assertAll(() -> {
            assertThat(foundOrderTable1.getTableGroupId()).isNull();
            assertThat(foundOrderTable1.isEmpty()).isFalse();
            assertThat(foundOrderTable2.getTableGroupId()).isNull();
            assertThat(foundOrderTable2.isEmpty()).isFalse();
        });
    }

    @ParameterizedTest
    @EnumSource(mode = EXCLUDE, names = {"COMPLETION"})
    void 단체_지정을_취소할_테이블들의_주문이_모두_완료_상태가_아닌_경우_취소할_수_없다(OrderStatus orderStatus) {
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(null, 1, true));
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(null, 2, true));

        TableGroup tableGroup = tableGroupService.create(
                new TableGroup(new ArrayList<>(Arrays.asList(orderTable1, orderTable2))));

        orderDao.save(new Order(orderTable1.getId(), orderStatus.name(), new ArrayList<>()));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
