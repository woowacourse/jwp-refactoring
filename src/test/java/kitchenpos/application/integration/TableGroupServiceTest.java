package kitchenpos.application.integration;

import kitchenpos.application.tablegroup.TableGroupService;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.dto.order.ChangeOrderStatusRequest;
import kitchenpos.dto.table.OrderTableRequest;
import kitchenpos.dto.table.OrderTableResponse;
import kitchenpos.dto.tablegroup.CreateTableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import kitchenpos.dto.tablegroup.UnGroupRequest;
import kitchenpos.exception.order.OrderTableNotFoundException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
public class TableGroupServiceTest extends ApplicationIntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    void create_table_group() {
        // given
        OrderTableResponse orderTable1 = createOrderTable(4, false);
        OrderTableResponse orderTable2 = createOrderTable(4, false);
        final CreateTableGroupRequest createTableGroupRequest = CreateTableGroupRequest.of(List.of(OrderTableRequest.of(orderTable1.getId()), OrderTableRequest.of(orderTable2.getId())));

        // when
        TableGroupResponse createdTableGroup = tableGroupService.create(createTableGroupRequest);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(createdTableGroup.getId()).isNotNull();
            softly.assertThat(createdTableGroup.getCreatedDate()).isNotNull();
            final List<OrderTableResponse> orderTables = createdTableGroup.getOrderTables();
            softly.assertThat(orderTables).hasSize(2);
            orderTables.forEach(orderTable -> {
                softly.assertThat(orderTable.isOrderable()).isTrue();
                softly.assertThat(orderTable.getTableGroupId()).isEqualTo(createdTableGroup.getId());
            });
        });
    }

    @Test
    void cannot_create_group_table_with_one_order_table() {
        // given
        OrderTableResponse orderTable = createOrderTable(4, false);
        final CreateTableGroupRequest createTableGroupRequest = CreateTableGroupRequest.of(List.of(OrderTableRequest.of(orderTable.getId())));
        // when & then
        assertThatThrownBy(() -> tableGroupService.create(createTableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(TableGroup.ORDER_TABLE_SIZE_IS_BELOW_TWO_ERROR_MESSAGE);
    }

    @Test
    void cannot_creat_group_table_with_non_orderable_order_table() {
        // given
        OrderTableResponse orderTable1 = createOrderTable(4, false);
        OrderTableResponse orderTable2 = createOrderTable(4, true);
        final CreateTableGroupRequest createTableGroupRequest = CreateTableGroupRequest.of(List.of(OrderTableRequest.of(orderTable1.getId()), OrderTableRequest.of(orderTable2.getId())));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(createTableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(TableGroup.ORDERABLE_TABLE_IS_NOT_ALLOWED_ERROR_MESSAGE);
    }

    @Test
    void cannot_create_with_not_saved_order_table() {
        // given
        OrderTableResponse orderTable1 = createOrderTable(4, false);
        final long orderTable2Id = 1000L;
        final CreateTableGroupRequest createTableGroupRequest = CreateTableGroupRequest.of(List.of(OrderTableRequest.of(orderTable1.getId()), OrderTableRequest.of(orderTable2Id)));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(createTableGroupRequest))
                .isInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    void cannot_create_with_already_grouped_order_table() {
        // given
        final OrderTableResponse orderTable1 = createOrderTable(4, false);
        final OrderTableResponse orderTable2 = createOrderTable(4, false);
        final CreateTableGroupRequest createTableGroupRequest = CreateTableGroupRequest.of(List.of(OrderTableRequest.of(orderTable1.getId()), OrderTableRequest.of(orderTable2.getId())));
        tableGroupService.create(createTableGroupRequest);
        final OrderTableResponse orderTable3 = createOrderTable(4, false);
        final CreateTableGroupRequest createTableGroupRequest2 = CreateTableGroupRequest.of(List.of(OrderTableRequest.of(orderTable3.getId()), OrderTableRequest.of(orderTable2.getId())));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(createTableGroupRequest2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(TableGroup.TABLE_IS_IN_TABLE_GROUP_ERROR_MESSAGE);
    }


    @Test
    void ungroup_table_group() {
        // given
        OrderTableResponse orderTable1 = createOrderTable(4, false);
        OrderTableResponse orderTable2 = createOrderTable(4, false);
        CreateTableGroupRequest createTableGroupRequest = CreateTableGroupRequest.of(List.of(OrderTableRequest.of(orderTable1.getId()), OrderTableRequest.of(orderTable2.getId())));
        TableGroupResponse createdTableGroup = tableGroupService.create(createTableGroupRequest);
        orderService.changeOrderStatus(
                createOrder(orderTable1.getId()).getId(),
                ChangeOrderStatusRequest.of(OrderStatus.COMPLETION));
        orderService.changeOrderStatus(
                createOrder(orderTable2.getId()).getId(),
                ChangeOrderStatusRequest.of(OrderStatus.COMPLETION));

        // when
        tableGroupService.ungroup(UnGroupRequest.of(createdTableGroup.getId()));

        // then
        SoftAssertions.assertSoftly(softly ->
                tableService.list().getOrderTables().forEach(orderTable -> {
                    softly.assertThat(orderTable.getTableGroupId()).isNull();
                    softly.assertThat(orderTable.isOrderable()).isFalse();
                }));
    }
}