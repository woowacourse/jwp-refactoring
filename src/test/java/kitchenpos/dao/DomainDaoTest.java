package kitchenpos.dao;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static java.util.Collections.singletonList;

public class DomainDaoTest extends DaoTest {

    private MenuDao menuDao;
    private OrderDao orderDao;
    private OrderTableDao orderTableDao;
    private TableGroupDao tableGroupDao;

    protected long SAVE_MENU_RETURN_ID() {
        menuDao = new JdbcTemplateMenuDao(dataSource);
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);

        Menu menu = new Menu("후라이드+후라이드", 19000, 1L, singletonList(menuProduct));

        // when - then
        Menu savedMenu = menuDao.save(menu);
        return savedMenu.getId();
    }

    protected long SAVE_ORDER_RETURN_ID() {
        // given
        orderDao = new JdbcTemplateOrderDao(dataSource);
        Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);

        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        // when
        Order savedOrder = orderDao.save(order);
        return savedOrder.getId();
    }

    protected long SAVE_ORDER_TABLE_RETURN_ID() {
        // given
        orderTableDao = new JdbcTemplateOrderTableDao(dataSource);
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(10);
        orderTable.setEmpty(false);

        // when
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        return savedOrderTable.getId();
    }

    protected long SAVE_TABLE_GROUP_RETURN_ID() {
        // given
        tableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());

        // when
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        return savedTableGroup.getId();
    }
}
