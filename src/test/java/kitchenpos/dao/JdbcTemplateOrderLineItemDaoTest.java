package kitchenpos.dao;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcTemplateOrderLineItemDaoTest extends JdbcTemplateTest {

    private OrderLineItemDao orderLineItemDao;
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        orderLineItemDao = new JdbcTemplateOrderLineItemDao(dataSource);
        orderDao = new JdbcTemplateOrderDao(dataSource);
    }

    @Test
    void 주문_항목을_저장한다() {
        Order order = makeOrder();
        Order savedOrder = orderDao.save(order);

        OrderLineItem orderLineItem = makeOrderLineItem(savedOrder);

        OrderLineItem saved = orderLineItemDao.save(orderLineItem);

        assertThat(saved.getOrderId()).isEqualTo(orderLineItem.getOrderId());
        assertThat(saved.getMenuId()).isEqualTo(orderLineItem.getMenuId());
        assertThat(saved.getQuantity()).isEqualTo(orderLineItem.getQuantity());

    }

    @Test
    void 식별자로_주문항목을_조회한다() {
        Order order = makeOrder();
        Order savedOrder = orderDao.save(order);

        OrderLineItem orderLineItem = makeOrderLineItem(savedOrder);
        OrderLineItem saved = orderLineItemDao.save(orderLineItem);

        OrderLineItem expected = orderLineItemDao.findById(saved.getSeq()).get();
        assertThat(expected.getSeq()).isEqualTo(saved.getSeq());
    }

    @Test
    void 모든_주문항목을_조회한다() {
        Order order = makeOrder();
        Order savedOrder = orderDao.save(order);

        OrderLineItem orderLineItem = makeOrderLineItem(savedOrder);
        orderLineItemDao.save(orderLineItem);
        orderLineItemDao.save(orderLineItem);

        List<OrderLineItem> orderLineItems = orderLineItemDao.findAll();
        assertThat(orderLineItems.size()).isEqualTo(2);
    }

    @Test
    void 주문_식별자로_주문항목들을_조회한다() {
        Order order = makeOrder();
        Order savedOrder = orderDao.save(order);

        OrderLineItem orderLineItem = makeOrderLineItem(savedOrder);
        orderLineItemDao.save(orderLineItem);
        orderLineItemDao.save(orderLineItem);

        List<OrderLineItem> itemsById = orderLineItemDao.findAllByOrderId(savedOrder.getId());
        assertThat(itemsById.size()).isEqualTo(2);

    }

    private OrderLineItem makeOrderLineItem(Order savedOrder) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(savedOrder.getId());
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1L);
        return orderLineItem;
    }

    private Order makeOrder() {
        Order order = new Order();
        order.setOrderStatus("진행중");
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(1L);
        return order;
    }
}
