package kitchenpos.dao;

import static kitchenpos.domain.OrderStatus.COOKING;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.sql.DataSource;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
public class JdbcTemplateTest {

    @Autowired
    protected DataSource dataSource;

    protected Product getProduct() {
        return new Product("후라이드", BigDecimal.valueOf(17000));
    }

    protected Order getOrder() {
        final Order order = new Order();
        order.setOrderStatus(COOKING.name());
        order.setOrderTableId(1L);
        order.setOrderedTime(LocalDateTime.now());
        return order;
    }

    protected OrderLineItem getOrderLineItem(final Long orderId) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(orderId);
        orderLineItem.setQuantity(1);
        orderLineItem.setMenuId(1L);
        return orderLineItem;
    }

    protected TableGroup getTableGroup() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }

    protected OrderTable getOrderTable(final Long tableGroupId) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(1);
        return orderTable;
    }
}
