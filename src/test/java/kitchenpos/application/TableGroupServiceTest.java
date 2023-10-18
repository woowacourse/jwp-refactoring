package kitchenpos.application;

import kitchenpos.common.service.ServiceTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
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
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

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
    void 주문_테이블이_널인경우_예외가_발생한다() {
        //when
        Assertions.assertThatThrownBy(() -> tableGroupService.create(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_사이즈가_2미만인_경우_예외가_발생한다() {
        //given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 0, true));

        //when
        Assertions.assertThatThrownBy(() -> tableGroupService.create(List.of(orderTable.getId())))
                .isInstanceOf(IllegalArgumentException.class);
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

        final Order order = new Order(orderTable.getId(), orderStatus.name(), LocalDateTime.now());
        orderRepository.save(order);

        //when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
