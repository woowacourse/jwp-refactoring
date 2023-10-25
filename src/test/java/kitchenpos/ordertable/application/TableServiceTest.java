package kitchenpos.ordertable.application;

import kitchenpos.common.service.ServiceTest;
import kitchenpos.order.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.repository.TableGroupRepository;
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
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    void OrderTable을_생성할_수_있다() {
        //when
        final Long orderTableId = tableService.create();

        //then
        assertThat(orderTableId).isNotNull();
    }

    @Test
    void OrderTable을_조회할_수_있다() {
        //given
        final OrderTable orderTableWithZero = new OrderTable(null, 0, true);
        final OrderTable orderTableWithOne = new OrderTable(null, 1, true);
        orderTableRepository.save(orderTableWithZero);
        orderTableRepository.save(orderTableWithOne);

        //when
        final List<OrderTable> list = tableService.list();

        //then
        assertThat(list).hasSize(2);
    }

    @Test
    void 주문_테이블을_비울_수_있다() {
        //given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable());

        //when
        tableService.changeEmpty(orderTable.getId(), true);

        //then
        final OrderTable changedOrderTable = orderTableRepository.findById(orderTable.getId()).get();
        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void 주문_상태가_COOKING이나MEAL이면_예외가_발생한다(OrderStatus orderStatus) {
        //given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 0, true));
        orderRepository.save(new Order(orderTable.getId(), orderStatus.name(), LocalDateTime.now()));

        //when, then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("orderTable이 존재하면서 조리중 또는 식사중인 주문 테이블은 empty 상태를 변경 할 수 없습니다.");
    }


    @Test
    void 주문_테이블의_손님_수를_변경할_수_있다() {
        //given
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(tableGroup.getId(), 0, false));

        //when
        tableService.changeNumberOfGuests(orderTable.getId(), 3);

        //then
        final OrderTable changedOrderTable = orderTableRepository.findById(orderTable.getId()).get();
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(3);
    }
}
