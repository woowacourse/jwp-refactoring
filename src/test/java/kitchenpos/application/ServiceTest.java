package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class ServiceTest {
    @Autowired
    protected TableService tableService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected OrderService orderService;

    protected OrderTable createOrderTable(
            final Long orderTableId,
            final Integer numberOfGuests,
            final Boolean empty
    ) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(orderTableId);
        if (numberOfGuests != null) {
            orderTable.setNumberOfGuests(numberOfGuests);
        }
        if (empty != null) {
            orderTable.setEmpty(empty);
        }
        return tableService.create(orderTable);
    }


    protected TableGroup createTableGroup(List<OrderTable> orderTables) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        return tableGroupService.create(tableGroup);
    }

    protected Order createOrder(
            final OrderTable orderTable,
            final List<Long> products
    ) {
        orderTable.setEmpty(false);

        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (Long product : products) {
            orderLineItems.add(createOrderLineItem(product));
        }

        final Order order = new Order();

        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(orderLineItems);

        return orderService.create(order);
    }

    private static OrderLineItem createOrderLineItem(final Long value) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(value);
        orderLineItem.setQuantity(value);
        orderLineItem.setSeq(value);
        orderLineItem.setMenuId(value);
        return orderLineItem;
    }
}
