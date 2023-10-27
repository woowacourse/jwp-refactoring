package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.request.TableGroupCreateRequest;
import kitchenpos.ui.response.TableGroupResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ServiceTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Test
    void 테이블_그룹을_생성한다() {
        // given
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 3, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 2, true));

        TableGroupCreateRequest tableGroupCreateRequest =
                new TableGroupCreateRequest(List.of(new TableGroupCreateRequest.OrderTableId(orderTable1.getId()),
                        new TableGroupCreateRequest.OrderTableId(orderTable2.getId())));

        // when
        TableGroupResponse tableGroup = tableGroupService.create(tableGroupCreateRequest);

        // then
        assertThat(tableGroup.getOrderTables()).usingRecursiveComparison()
                .ignoringFields("id", "tableGroup")
                .isEqualTo(List.of(new OrderTable(null, 3, false), new OrderTable(null, 2, false)));
    }

    @Test
    void 하나의_테이블은_그룹화할_수_없다() {
        // given
        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 3, true));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupCreateRequest(List.of(new TableGroupCreateRequest.OrderTableId(orderTable.getId())))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_하나라도_다른_테이블에_속해있으면_그룹화할_수_없다() {
        // given
        TableGroup tableGroup = tableGroupRepository.save(TableGroup.of(LocalDateTime.now(), new OrderTables(Collections.emptyList())));
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, tableGroup, 3, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 3, true));

        TableGroupCreateRequest tableGroupCreateRequest =
                new TableGroupCreateRequest(List.of(new TableGroupCreateRequest.OrderTableId(orderTable1.getId()),
                        new TableGroupCreateRequest.OrderTableId(orderTable2.getId())));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹을_해제한다() {
        // given
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 3, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 2, true));

        TableGroupCreateRequest tableGroupCreateRequest =
                new TableGroupCreateRequest(List.of(new TableGroupCreateRequest.OrderTableId(orderTable1.getId()),
                        new TableGroupCreateRequest.OrderTableId(orderTable2.getId())));

        TableGroupResponse tableGroup = tableGroupService.create(tableGroupCreateRequest);

        // when
        tableGroupService.ungroup(tableGroup.getId());

        OrderTable unGroupedOrderTable1 = orderTableRepository.findById(orderTable1.getId()).get();
        OrderTable unGroupedOrderTable2 = orderTableRepository.findById(orderTable2.getId()).get();

        // then
        assertAll(
                () -> assertEquals(unGroupedOrderTable1.getTableGroup(), null),
                () -> assertEquals(unGroupedOrderTable1.isEmpty(), false),
                () -> assertEquals(unGroupedOrderTable2.getTableGroup(), null),
                () -> assertEquals(unGroupedOrderTable2.isEmpty(), false)
        );
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"MEAL", "COOKING"})
    void 그룹으로_묶여있는_테이블의_주문_상태가_하나라도_MEAL_이나_COOKING_상태면_그룹화를_해제할_수_없다(OrderStatus status) {
        // given
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 3, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 2, true));

        TableGroupCreateRequest tableGroupCreateRequest =
                new TableGroupCreateRequest(List.of(new TableGroupCreateRequest.OrderTableId(orderTable1.getId()),
                        new TableGroupCreateRequest.OrderTableId(orderTable2.getId())));

        TableGroupResponse tableGroup = tableGroupService.create(tableGroupCreateRequest);

        orderRepository.save(new Order(orderTable1, status));

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
