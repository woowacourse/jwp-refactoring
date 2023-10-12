package kitchenpos.dao;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class OrderLineItemDaoTest {
    @Autowired
    private DataSource dataSource;
    private OrderLineItemDao orderLineItemDao;
    private OrderDao orderDao;
    private Order order;

    @BeforeEach
    void setUp() {
        orderLineItemDao = new JdbcTemplateOrderLineItemDao(dataSource);
        orderDao = new JdbcTemplateOrderDao(dataSource);
        final Order entity = new Order();
        entity.setOrderTableId(1L);
        entity.setOrderStatus("MEAL");
        entity.setOrderedTime(LocalDateTime.now());
        order = orderDao.save(entity);

    }

    @Test
    @DisplayName("주문 상세 정보를 저장한다.")
    public void save() {
        //given
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(order.getId());
        orderLineItem.setMenuId(2L);
        orderLineItem.setQuantity(3L);

        //when
        OrderLineItem returnedOrderLineItem = orderLineItemDao.save(orderLineItem);

        //then
        assertThat(returnedOrderLineItem.getSeq()).isNotNull();
        assertThat(orderLineItem.getOrderId()).isEqualTo(returnedOrderLineItem.getOrderId());
        assertThat(orderLineItem.getMenuId()).isEqualTo(returnedOrderLineItem.getMenuId());
        assertThat(orderLineItem.getQuantity()).isEqualTo(returnedOrderLineItem.getQuantity());
    }

    @Test
    @DisplayName("주문 상세 정보를 아이디로 찾는다.")
    public void findById() {
        //given
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(order.getId());
        orderLineItem.setMenuId(2L);
        orderLineItem.setQuantity(3L);
        Long seq = orderLineItemDao.save(orderLineItem).getSeq();

        //when
        Optional<OrderLineItem> returnedOrderLineItem = orderLineItemDao.findById(seq);

        //then
        assertThat(returnedOrderLineItem).isPresent();
        assertThat(returnedOrderLineItem.get().getOrderId()).isEqualTo(orderLineItem.getOrderId());
        assertThat(returnedOrderLineItem.get().getMenuId()).isEqualTo(orderLineItem.getMenuId());
        assertThat(returnedOrderLineItem.get().getQuantity()).isEqualTo(orderLineItem.getQuantity());
    }

    @Test
    @DisplayName("모든 주문 상세 정보를 찾는다.")
    public void findAll() {
        //given
        final int originalSize = orderLineItemDao.findAll().size();
        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setOrderId(order.getId());
        orderLineItem1.setMenuId(2L);
        orderLineItem1.setQuantity(3L);
        orderLineItemDao.save(orderLineItem1);

        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setOrderId(order.getId());
        orderLineItem2.setMenuId(5L);
        orderLineItem2.setQuantity(6L);
        orderLineItemDao.save(orderLineItem2);

        //when
        List<OrderLineItem> returnedOrderLineItems = orderLineItemDao.findAll();

        //then
        assertThat(returnedOrderLineItems).hasSize(2 + originalSize);
        assertThat(returnedOrderLineItems.get(0).getOrderId()).isEqualTo(orderLineItem1.getOrderId());
        assertThat(returnedOrderLineItems.get(0).getMenuId()).isEqualTo(orderLineItem1.getMenuId());
        assertThat(returnedOrderLineItems.get(0).getQuantity()).isEqualTo(orderLineItem1.getQuantity());
        assertThat(returnedOrderLineItems.get(1).getOrderId()).isEqualTo(orderLineItem2.getOrderId());
        assertThat(returnedOrderLineItems.get(1).getMenuId()).isEqualTo(orderLineItem2.getMenuId());
        assertThat(returnedOrderLineItems.get(1).getQuantity()).isEqualTo(orderLineItem2.getQuantity());
    }

    @Test
    @DisplayName("주문 아이디로 주문 상세 정보를 찾는다.")
    public void findAllByOrderId() {
        //given
        final int originalSize = orderLineItemDao.findAll().size();
        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setOrderId(order.getId());
        orderLineItem1.setMenuId(2L);
        orderLineItem1.setQuantity(3L);
        orderLineItemDao.save(orderLineItem1);

        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setOrderId(order.getId());
        orderLineItem2.setMenuId(5L);
        orderLineItem2.setQuantity(6L);
        orderLineItemDao.save(orderLineItem2);

        OrderLineItem orderLineItem3 = new OrderLineItem();
        orderLineItem3.setOrderId(order.getId());
        orderLineItem3.setMenuId(5L);
        orderLineItem3.setQuantity(6L);
        orderLineItemDao.save(orderLineItem3);

        //when
        List<OrderLineItem> returnedOrderLineItems = orderLineItemDao.findAllByOrderId(order.getId());

        //then
        assertThat(returnedOrderLineItems).hasSize(3 + originalSize);
        assertThat(returnedOrderLineItems.get(0).getOrderId()).isEqualTo(orderLineItem1.getOrderId());
        assertThat(returnedOrderLineItems.get(0).getMenuId()).isEqualTo(orderLineItem1.getMenuId());
        assertThat(returnedOrderLineItems.get(0).getQuantity()).isEqualTo(orderLineItem1.getQuantity());
        assertThat(returnedOrderLineItems.get(1).getOrderId()).isEqualTo(orderLineItem2.getOrderId());
        assertThat(returnedOrderLineItems.get(1).getMenuId()).isEqualTo(orderLineItem2.getMenuId());
        assertThat(returnedOrderLineItems.get(1).getQuantity()).isEqualTo(orderLineItem2.getQuantity());
    }
}
