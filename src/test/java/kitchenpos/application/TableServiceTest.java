package kitchenpos.application;

import kitchenpos.common.service.ServiceTest;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Test
    void OrderTable을_생성할_수_있다() {
        //when
        final OrderTable orderTable = tableService.create(new OrderTable(null, 0, true));

        //then
        assertThat(orderTable.getId()).isNotNull();
    }

    @Test
    void OrderTable을_조회할_수_있다() {
        //given
        final OrderTable orderTableWithZero = new OrderTable(null, 0, true);
        final OrderTable orderTableWithOne = new OrderTable(null, 1, true);
        orderTableDao.save(orderTableWithZero);
        orderTableDao.save(orderTableWithOne);

        //when
        final List<OrderTable> list = tableService.list();

        //then
        assertThat(list).hasSize(2);
    }

    @Test
    void 주문_테이블을_비울_수_있다() {
        //given
        final OrderTable orderTable = tableService.create(new OrderTable(null, 0, false));

        //when
        final OrderTable saveOrderTable = tableService.changeEmpty(orderTable.getId(),
                new OrderTable(null, 0, true));

        //then
        assertThat(saveOrderTable.isEmpty()).isTrue();
    }

    @Test
    void 주문_테이블의_단체_지정_id가_널이_아니면_예외가_발생한다() {
        //given
        final TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 0, true));

        //when, then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void 주문_상태가_COOKING이나MEAL이면_예외가_발생한다(OrderStatus orderStatus) {
        //given
        final TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 0, true));
        orderDao.save(new Order(orderTable.getId(), orderStatus.name(), LocalDateTime.now(), null));

        //when, then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), null))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void 주문_테이블의_손님_수를_변경할_수_있다() {
        //given
        final TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 0, false));

        //when
        final OrderTable saveOrderTable = tableService.changeNumberOfGuests(orderTable.getId(),
                new OrderTable(null, 3, false));

        //then
        assertThat(saveOrderTable.getNumberOfGuests()).isEqualTo(3);
    }

    @Test
    void 손님의_수가_0미만인_경우_예외가_발생한다() {
        //given
        final TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 0, false));

        //when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(),
                new OrderTable(null, -1, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_비어있으면_예외가_발생한다() {
        //given
        final TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 0, true));

        //when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(),
                new OrderTable(null, 3, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
