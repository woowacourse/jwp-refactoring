package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderLineItemTest extends DaoTest {

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    void save() throws Exception {
        OrderTable orderTable = orderTableDao.save(new OrderTable());
        Order order = orderDao.save(new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now()));
        Menu menu = menuDao.save(new Menu("메뉴명", new BigDecimal(10000), 1L));
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(new OrderLineItem(order, menu.getId(), 10));
        OrderLineItem foundOrderLineItem = orderLineItemDao.findById(savedOrderLineItem.getSeq())
            .orElseThrow(() -> new Exception());
        assertThat(savedOrderLineItem.getSeq()).isEqualTo(foundOrderLineItem.getSeq());
        assertThat(savedOrderLineItem.getOrderId()).isEqualTo(foundOrderLineItem.getOrderId());
        assertThat(savedOrderLineItem.getMenuId()).isEqualTo(foundOrderLineItem.getMenuId());
        assertThat(savedOrderLineItem.getQuantity()).isEqualTo(foundOrderLineItem.getQuantity());
    }

    @Test
    void findById() throws Exception {
        OrderTable orderTable = orderTableDao.save(new OrderTable());
        Order order = orderDao.save(new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now()));
        Menu menu = menuDao.save(new Menu("메뉴명", new BigDecimal(10000), 1L));
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(new OrderLineItem(order, menu.getId(), 10));
        OrderLineItem foundOrderLineItem = orderLineItemDao.findById(savedOrderLineItem.getSeq())
            .orElseThrow(() -> new Exception());
        assertThat(savedOrderLineItem.getSeq()).isEqualTo(foundOrderLineItem.getSeq());
        assertThat(savedOrderLineItem.getOrderId()).isEqualTo(foundOrderLineItem.getOrderId());
        assertThat(savedOrderLineItem.getMenuId()).isEqualTo(foundOrderLineItem.getMenuId());
        assertThat(savedOrderLineItem.getQuantity()).isEqualTo(foundOrderLineItem.getQuantity());
    }

    @Test
    void findAll() {
        OrderTable orderTable = orderTableDao.save(new OrderTable());
        Order order = orderDao.save(new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now()));
        Menu menu = menuDao.save(new Menu("메뉴명", new BigDecimal(10000), 1L));
        orderLineItemDao.save(new OrderLineItem(order, menu.getId(), 10));
        orderLineItemDao.save(new OrderLineItem(order, menu.getId(), 10));
        assertThat(orderLineItemDao.findAll()).hasSize(2);
    }

    @Test
    void findAllByOrderId() {
        OrderTable orderTable = orderTableDao.save(new OrderTable());
        Order order = orderDao.save(new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now()));
        Menu menu = menuDao.save(new Menu("메뉴명", new BigDecimal(10000), 1L));
        orderLineItemDao.save(new OrderLineItem(order, menu.getId(), 10));
        orderLineItemDao.save(new OrderLineItem(order, menu.getId(), 10));
        assertThat(orderLineItemDao.findAllByOrderId(order.getId())).hasSize(2);
    }
}
