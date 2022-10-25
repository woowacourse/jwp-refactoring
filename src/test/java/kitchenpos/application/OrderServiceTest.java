package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    void list() {
        Menu menu = givenMenu();

        OrderTable orderTable = givenTable();
        Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        Order savedOrder = orderDao.save(order);

        givenOrderLineItem(menu, savedOrder);
        givenOrderLineItem(menu, savedOrder);

        List<Order> orders = orderService.list();

        assertThat(orders).hasSize(1);
    }

    private Menu givenMenu() {
        MenuGroup menuGroup = givenMenuGroup();
        Menu menu = new Menu();
        menu.setName("치킨");
        menu.setPrice(BigDecimal.valueOf(1700));
        menu.setMenuGroupId(menuGroup.getId());
        return menuDao.save(menu);
    }

    private MenuGroup givenMenuGroup() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("치킨메뉴");
        return menuGroupDao.save(menuGroup);
    }

    private OrderTable givenTable() {
        TableGroup tableGroup = givenTableGroup();
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroup.getId());
        orderTable.setNumberOfGuests(10);
        orderTable.setEmpty(true);
        return orderTableDao.save(orderTable);
    }

    private TableGroup givenTableGroup() {
        TableGroup tg = new TableGroup();
        tg.setCreatedDate(LocalDateTime.now());
        return tableGroupDao.save(tg);
    }

    private OrderLineItem givenOrderLineItem(final Menu menu, final Order order) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(order.getId());
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(1);
        return orderLineItemDao.save(orderLineItem);
    }
}
