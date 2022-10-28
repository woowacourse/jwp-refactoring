package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderDao orderDao;

    @Test
    void 테이블을_단체로_지정할_수_있다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 1, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 2, true));

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                new ArrayList<>(Arrays.asList(orderTable1, orderTable2)));

        TableGroup actual = tableGroupService.create(tableGroup);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getOrderTables()).hasSize(2);
        });
    }

    @Test
    void 단체로_지정할_테이블이_한_개_이하인_경우_지정할_수_없다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 1, true));

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), new ArrayList<>(Arrays.asList(orderTable1)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체로_지정할_테이블_중_빈_테이블이_존재하는_경우_지정할_수_없다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 1, false));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 2, true));

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                new ArrayList<>(Arrays.asList(orderTable1, orderTable2)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체로_지정할_테이블_중_이미_단체로_지정된_테이블이_존재하는_경우_지정할_수_없다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 1, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 2, true));

        TableGroup tableGroup1 = tableGroupRepository.save(
                new TableGroup(LocalDateTime.now(), new ArrayList<>(Arrays.asList(orderTable1, orderTable2))));

        OrderTable orderTable3 = orderTableRepository.save(new OrderTable(tableGroup1, 1, true));
        OrderTable orderTable4 = orderTableRepository.save(new OrderTable(null, 1, true));

        TableGroup tableGroup2 = new TableGroup(LocalDateTime.now(),
                new ArrayList<>(Arrays.asList(orderTable3, orderTable4)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup2)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_단체_지정을_취소할_수_있다() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 1, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 2, true));

        TableGroup tableGroup = tableGroupRepository.save(
                new TableGroup(LocalDateTime.now(), new ArrayList<>(Arrays.asList(orderTable1, orderTable2))));
        orderTableRepository.save(
                new OrderTable(orderTable1.getId(), tableGroup, orderTable1.getNumberOfGuests(),
                        orderTable1.isEmpty()));
        orderTableRepository.save(
                new OrderTable(orderTable2.getId(), tableGroup, orderTable2.getNumberOfGuests(),
                        orderTable2.isEmpty()));

        tableGroupService.ungroup(tableGroup.getId());

        OrderTable foundOrderTable1 = orderTableRepository.findById(orderTable1.getId()).get();
        OrderTable foundOrderTable2 = orderTableRepository.findById(orderTable2.getId()).get();

        assertAll(() -> {
            assertThat(foundOrderTable1.getTableGroup()).isNull();
            assertThat(foundOrderTable1.isEmpty()).isFalse();
            assertThat(foundOrderTable2.getTableGroup()).isNull();
            assertThat(foundOrderTable2.isEmpty()).isFalse();
        });
    }

    @ParameterizedTest
    @EnumSource(mode = EXCLUDE, names = {"COMPLETION"})
    void 단체_지정을_취소할_테이블들의_주문이_모두_완료_상태가_아닌_경우_취소할_수_없다(OrderStatus orderStatus) {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 1, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 2, true));

        TableGroup tableGroup = tableGroupRepository.save(
                new TableGroup(LocalDateTime.now(), new ArrayList<>(Arrays.asList(orderTable1, orderTable2))));
        orderTableRepository.save(
                new OrderTable(orderTable1.getId(), tableGroup, orderTable1.getNumberOfGuests(),
                        orderTable1.isEmpty()));
        orderTableRepository.save(
                new OrderTable(orderTable2.getId(), tableGroup, orderTable2.getNumberOfGuests(),
                        orderTable2.isEmpty()));

        orderDao.save(new Order(orderTable1.getId(), orderStatus.name(), new ArrayList<>()));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
