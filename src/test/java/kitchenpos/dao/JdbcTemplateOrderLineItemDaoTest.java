package kitchenpos.dao;

import static kitchenpos.dao.DomainCreator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@JdbcTest
class JdbcTemplateOrderLineItemDaoTest {
    private DataSource dataSource;
    private JdbcTemplateOrderLineItemDao orderLineItemDao;
    private JdbcTemplateOrderDao orderDao;
    private JdbcTemplateMenuDao menuDao;
    private JdbcTemplateMenuGroupDao menuGroupDao;
    private JdbcTemplateOrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
            .addScript("classpath:delete.sql")
            .addScript("classpath:initialize.sql")
            .build();
        orderLineItemDao = new JdbcTemplateOrderLineItemDao(dataSource);
        orderDao = new JdbcTemplateOrderDao(dataSource);
        menuDao = new JdbcTemplateMenuDao(dataSource);
        menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
        orderTableDao = new JdbcTemplateOrderTableDao(dataSource);

        OrderTable orderTable = createOrderTable(true);
        orderTableDao.save(orderTable); //1L

        Order order = createOrder(OrderStatus.COOKING, 1L);
        orderDao.save(order); //1L

        MenuGroup menuGroup = createMenuGroup("menuGroup");
        menuGroupDao.save(menuGroup); //1L

        Menu menu = createMenu("menu", 1L, BigDecimal.valueOf(1000));
        menuDao.save(menu); //1L
    }

    @Test
    void save() {
        OrderLineItem orderLineItem = createOrderLineItem(1L, 1L);
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);

        assertAll(
            () -> assertThat(savedOrderLineItem.getSeq()).isNotNull(),
            () -> assertThat(savedOrderLineItem.getMenuId()).isEqualTo(orderLineItem.getMenuId()),
            () -> assertThat(savedOrderLineItem.getOrderId()).isEqualTo(orderLineItem.getOrderId())
        );
    }

    @Test
    void findById() {
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(createOrderLineItem(1L, 1L));
        OrderLineItem expectedOrderLineItem = orderLineItemDao.findById(savedOrderLineItem.getSeq()).get();

        assertAll(
            () -> assertThat(expectedOrderLineItem.getSeq()).isEqualTo(savedOrderLineItem.getSeq()),
            () -> assertThat(expectedOrderLineItem.getOrderId()).isEqualTo(savedOrderLineItem.getOrderId()),
            () -> assertThat(expectedOrderLineItem.getMenuId()).isEqualTo(savedOrderLineItem.getMenuId())
        );
    }

    @Test
    void findAll() {
        orderLineItemDao.save(createOrderLineItem(1L, 1L));
        orderLineItemDao.save(createOrderLineItem(1L, 1L));

        assertThat(orderLineItemDao.findAll().size()).isEqualTo(2);
    }

    @Test
    void findAllByOrderId() {
        orderDao.save(createOrder(OrderStatus.COOKING, 1L)); //2L
        Order order = orderDao.save(createOrder(OrderStatus.COOKING, 1L)); //3L

        orderLineItemDao.save(createOrderLineItem(1L, 1L));
        orderLineItemDao.save(createOrderLineItem(order.getId(), 1L));

        assertThat(orderLineItemDao.findAllByOrderId(order.getId()).size()).isEqualTo(1);
    }
}
