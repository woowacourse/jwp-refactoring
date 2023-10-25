package kitchenpos.tablegroup.application;

import kitchenpos.common.service.ServiceTest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
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
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void TableGroup을_생성할_수_있다() {
        //given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 0, true));
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 0, true));

        //when
        final Long tableGroupId = tableGroupService.create(List.of(orderTable.getId(), orderTable1.getId()));

        //then
        assertThat(tableGroupId).isNotNull();
    }

    @Test
    void 단체_지정을_풀_수_있다() {
        //given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 0, true));
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 0, true));
        final Long tableGroupId = tableGroupService.create(List.of(orderTable.getId(), orderTable1.getId()));

        //when
        tableGroupService.ungroup(tableGroupId);

        //then
        assertAll(
                () -> assertThat(orderTableRepository.findById(orderTable.getId()).get().getTableGroupId()).isNull(),
                () -> assertThat(orderTableRepository.findById(orderTable1.getId()).get().getTableGroupId()).isNull(),
                () -> assertThat(orderTableRepository.findById(orderTable.getId()).get().isEmpty()).isFalse(),
                () -> assertThat(orderTableRepository.findById(orderTable1.getId()).get().isEmpty()).isFalse()
        );
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void 주문_상태가_COOKING이나MEAL이면_예외가_발생한다(OrderStatus orderStatus) {
        //given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 0, true));
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 0, true));
        final Long tableGroupId = tableGroupService.create(List.of(orderTable.getId(), orderTable1.getId()));

        final Order order = new Order(orderTable.getId(), orderStatus, LocalDateTime.now());
        orderRepository.save(order);

        //when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 orderTable이거나 table주문 상태가 조리중 또는 식사중인 테이블 그룹은 해체할 수 없습니다.");
    }
}
