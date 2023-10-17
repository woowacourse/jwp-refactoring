package kitchenpos.application;

import kitchenpos.common.service.ServiceTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Test
    void TableGroup을_생성할_수_있다() {
        //given
        final OrderTable orderTable = orderTableDao.save(new OrderTable(null, 0, true));
        final OrderTable orderTable1 = orderTableDao.save(new OrderTable(null, 0, true));
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                List.of(orderTable, orderTable1));

        //when
        final TableGroup saveTableGroup = tableGroupService.create(tableGroup);

        //then
        assertThat(saveTableGroup.getId()).isNotNull();
    }

    @Test
    void 주문_테이블이_널인경우_예외가_발생한다() {
        //given
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), null);

        //when
        Assertions.assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_사이즈가_2미만인_경우_예외가_발생한다() {
        //given
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                List.of(new OrderTable(null, 0, true)));

        //when
        Assertions.assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_사이즈가_DB에_저장된_사이즈와_다른_경우_예외가_발생한다() {
        //given
        final TableGroup saveTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(saveTableGroup.getId(), 0, true));
        final OrderTable orderTable1 = orderTableDao.save(new OrderTable(saveTableGroup.getId(), 0, true));

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                List.of(orderTable, orderTable, orderTable1));

        //when, then
        Assertions.assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_비어있지_않은_경우_예외가_발생한다() {
        //given
        final TableGroup saveTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(saveTableGroup.getId(), 0, false));
        final OrderTable orderTable1 = orderTableDao.save(new OrderTable(saveTableGroup.getId(), 0, false));

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                List.of(orderTable, orderTable1));

        //when, then
        Assertions.assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_단체_지정아이디가_널이_아닌_경우_예외가_발생한다() {
        //given
        final TableGroup saveTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(saveTableGroup.getId(), 0, true));
        final OrderTable orderTable1 = orderTableDao.save(new OrderTable(saveTableGroup.getId(), 0, true));

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                List.of(orderTable, orderTable1));

        //when, then
        Assertions.assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정을_풀_수_있다() {
        //given
        final OrderTable orderTable = orderTableDao.save(new OrderTable(null, 0, true));
        final OrderTable orderTable1 = orderTableDao.save(new OrderTable(null, 0, true));
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                List.of(orderTable, orderTable1));
        final TableGroup saveTableGroup = tableGroupService.create(tableGroup);

        //when
        tableGroupService.ungroup(saveTableGroup.getId());

        //then
        assertAll(
                () -> assertThat(orderTableDao.findById(orderTable.getId()).get().getTableGroupId()).isNull(),
                () -> assertThat(orderTableDao.findById(orderTable1.getId()).get().getTableGroupId()).isNull(),
                () -> assertThat(orderTableDao.findById(orderTable.getId()).get().isEmpty()).isFalse(),
                () -> assertThat(orderTableDao.findById(orderTable1.getId()).get().isEmpty()).isFalse()
        );
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void 주문_상태가_COOKING이나MEAL이면_예외가_발생한다(OrderStatus orderStatus) {
        //given
        final OrderTable orderTable = orderTableDao.save(new OrderTable(null, 0, true));
        final OrderTable orderTable1 = orderTableDao.save(new OrderTable(null, 0, true));
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                List.of(orderTable, orderTable1));
        final TableGroup saveTableGroup = tableGroupService.create(tableGroup);

        final Order order = new Order(orderTable.getId(), orderStatus.name(), LocalDateTime.now());
        orderDao.save(order);

        //when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(saveTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
