package kitchenpos.dao;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

public class DomainDaoTest extends DaoTest{

    private MenuDao menuDao;
    private OrderDao orderDao;

    protected long SAVE_MENU() {
        menuDao = new JdbcTemplateMenuDao(dataSource);
        Menu menu = new Menu();
        menu.setName("후라이드+후라이드");
        menu.setPrice(BigDecimal.valueOf(19000));
        menu.setMenuGroupId(1L);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        // when - then
        Menu savedMenu = menuDao.save(menu);
        return savedMenu.getId();
    }

    protected long SAVE_ORDER() {
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
}
