package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.application.TableGroupService;
import kitchenpos.order.application.dto.TableGroupCreationDto;
import kitchenpos.order.application.dto.TableGroupDto;
import kitchenpos.common.annotation.SpringTestWithData;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.ui.dto.request.OrderTableIdDto;
import kitchenpos.order.ui.dto.request.TableGroupCreationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("TableGroupService 는 ")
@SpringTestWithData
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void createTableGroup() {
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(0, true));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(0, true));
        final TableGroupCreationRequest tableGroupCreationRequest = new TableGroupCreationRequest(
                List.of(new OrderTableIdDto(orderTable1.getId()), new OrderTableIdDto(orderTable2.getId())));
        final TableGroupCreationDto tableGroupCreationDto = TableGroupCreationDto.from(tableGroupCreationRequest);

        final TableGroupDto tableGroupDto = tableGroupService.create(tableGroupCreationDto);

        assertThat(tableGroupDto.getOrderTables().size()).isEqualTo(2);
    }

    @DisplayName("테이블 그룹을 해제할 때 ")
    @SpringTestWithData
    @Nested
    class TableUngroupTest {

        @DisplayName("해제 가능한 조건이면 그룹을 해제한다.")
        @Test
        void ungroupTableSuccess() {
            final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(0, true));
            final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(0, true));
            final TableGroup tableGroup = tableGroupRepository.save(
                    new TableGroup(List.of(orderTable1, orderTable2), LocalDateTime.now()));

            assertDoesNotThrow(() -> tableGroupService.ungroupTable(tableGroup.getId()));
        }

        @DisplayName("그룹에 있는 주문의 상태가 COMPLETION이 아니면 에러를 던진다.")
        @Test
        void ungorupTableFail() {
            final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(0, true));
            final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(0, true));
            final Order order = orderRepository.save(new Order(orderTable1.getId(), OrderStatus.COOKING, LocalDateTime.now(),
                    List.of(new OrderLineItem(null, 1L))));
            final TableGroup tableGroup = tableGroupRepository.save(
                    new TableGroup(List.of(orderTable1, orderTable2), LocalDateTime.now()));
            orderTableRepository.save(new OrderTable(orderTable1.getId(), tableGroup.getId(), orderTable1.getNumberOfGuests(),
                    orderTable1.isEmpty()));
            orderTableRepository.save(new OrderTable(orderTable2.getId(), tableGroup.getId(), orderTable1.getNumberOfGuests(),
                    orderTable1.isEmpty()));

            assertThatThrownBy(() -> tableGroupService.ungroupTable(tableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);

        }
    }

}
