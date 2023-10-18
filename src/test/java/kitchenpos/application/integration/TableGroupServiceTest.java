package kitchenpos.application.integration;

import kitchenpos.application.TableGroupService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class TableGroupServiceTest extends ApplicationIntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    void create_table_group() {
        // given
        OrderTable orderTable1 = createOrderTable(4, true);
        OrderTable orderTable2 = createOrderTable(4, true);
        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable1, orderTable2));

        // when
        TableGroup createdTableGroup = tableGroupService.create(tableGroup);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(createdTableGroup.getId()).isNotNull();
            softly.assertThat(createdTableGroup.getCreatedDate()).isNotNull();
            final List<OrderTable> orderTables = createdTableGroup.getOrderTables();
            softly.assertThat(orderTables).hasSize(2);
            orderTables.forEach(orderTable -> {
                softly.assertThat(orderTable.isEmpty()).isFalse();
                softly.assertThat(orderTable.getTableGroupId()).isEqualTo(createdTableGroup.getId());
            });
        });
    }

    @Test
    void cannot_create_group_table_with_one_order_table() {
        // given
        OrderTable orderTable = createOrderTable(4, true);
        TableGroup tableGroup = new TableGroup(List.of(orderTable));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cannot_creat_group_table_with_non_empty_order_table() {
        // given
        OrderTable orderTable1 = createOrderTable(4, false);
        OrderTable orderTable2 = createOrderTable(4, true);
        TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cannot_create_with_not_saved_order_table() {
        // given
        OrderTable orderTable1 = createOrderTable(4, true);
        OrderTable orderTable2 = new OrderTable(4, true);
        TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cannot_create_with_already_grouped_order_table() {
        // given
        OrderTable orderTable1 = createOrderTable(4, true);
        OrderTable orderTable2 = createOrderTable(4, true);
        TableGroup tableGroup1 = new TableGroup(List.of(orderTable1, orderTable2));
        tableGroupService.create(tableGroup1);
        TableGroup tableGroup2 = new TableGroup(List.of(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void ungroup_table_group() {
        // given
        OrderTable orderTable1 = createOrderTable(4, true);
        OrderTable orderTable2 = createOrderTable(4, true);
        TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));
        TableGroup createdTableGroup = tableGroupService.create(tableGroup);
        orderService.changeOrderStatus(
                createOrder(orderTable1.getId()).getId(),
                new Order(OrderStatus.COMPLETION.name())
        );
        orderService.changeOrderStatus(
                createOrder(orderTable2.getId()).getId(),
                new Order(OrderStatus.COMPLETION.name())
        );

        // when
        tableGroupService.ungroup(createdTableGroup.getId());

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(orderTable1.isEmpty()).isTrue();
            softly.assertThat(orderTable1.getTableGroupId()).isNull();
            softly.assertThat(orderTable2.isEmpty()).isTrue();
            softly.assertThat(orderTable2.getTableGroupId()).isNull();
        });
    }

    @Test
    void cannot_ungroup_table_group_with_cooking_order_status() {
        // given
        OrderTable orderTable1 = createOrderTable(4, true);
        OrderTable orderTable2 = createOrderTable(4, true);
        TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));
        TableGroup createdTableGroup = tableGroupService.create(tableGroup);
        orderService.changeOrderStatus(
                createOrder(orderTable1.getId()).getId(),
                new Order(OrderStatus.COOKING.name())
        );
        orderService.changeOrderStatus(
                createOrder(orderTable2.getId()).getId(),
                new Order(OrderStatus.COMPLETION.name())
        );

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(createdTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cannot_un_group_table_group_with_meal_order_status() {
        // given
        OrderTable orderTable1 = createOrderTable(4, true);
        OrderTable orderTable2 = createOrderTable(4, true);
        TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));
        TableGroup createdTableGroup = tableGroupService.create(tableGroup);
        orderService.changeOrderStatus(
                createOrder(orderTable1.getId()).getId(),
                new Order(OrderStatus.MEAL.name())
        );
        orderService.changeOrderStatus(
                createOrder(orderTable2.getId()).getId(),
                new Order(OrderStatus.COMPLETION.name())
        );

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(createdTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
