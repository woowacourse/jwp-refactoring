package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

@SpringBootTest
class JdbcTemplateOrderLineItemDaoTest {
    @Autowired
    private JdbcTemplateOrderLineItemDao orderLineItemDao;
    @Autowired
    private JdbcTemplateOrderDao orderDao;

    private OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        Order order = new Order();
        orderLineItem = new OrderLineItem();

        order.setOrderTableId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

        Order savedOrder = orderDao.save(order);
        orderLineItem.setOrderId(savedOrder.getId());
        orderLineItem.setMenuId(1L);
    }

    @Test
    void save() {
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);

        assertThat(savedOrderLineItem.getSeq()).isNotNull();
        assertThat(savedOrderLineItem.getMenuId()).isEqualTo(orderLineItem.getMenuId());
        assertThat(savedOrderLineItem.getOrderId()).isEqualTo(orderLineItem.getOrderId());
    }

    @Test
    void findById() {
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);
        OrderLineItem foundOrderLineItem = orderLineItemDao.findById(savedOrderLineItem.getSeq()).get();

        assertThat(foundOrderLineItem.getSeq()).isEqualTo(savedOrderLineItem.getSeq());
        assertThat(foundOrderLineItem.getOrderId()).isEqualTo(savedOrderLineItem.getOrderId());
        assertThat(foundOrderLineItem.getMenuId()).isEqualTo(savedOrderLineItem.getMenuId());
    }

    @Test
    void findAll() {
        int orderLineItemCount = orderLineItemDao.findAll().size();
        orderLineItemDao.save(orderLineItem);
        assertThat(orderLineItemDao.findAll().size()).isEqualTo(orderLineItemCount + 1);
    }

    @Test
    void findAllByOrderId() {
        Order order = new Order();
        OrderLineItem orderLineItem2 = new OrderLineItem();

        order.setOrderTableId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        Order savedOrder2 = orderDao.save(order);
        orderLineItem2.setOrderId(savedOrder2.getId());
        orderLineItem2.setMenuId(1L);

        orderLineItemDao.save(orderLineItem);
        orderLineItemDao.save(orderLineItem2);

        assertThat(orderLineItemDao.findAllByOrderId(savedOrder2.getId()).size()).isEqualTo(1);
    }
}
