package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.support.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class OrderLineItemDaoTest {

    private OrderLineItemDao orderLineItemDao;
    private OrderDao orderDao;

    @Autowired
    public OrderLineItemDaoTest(OrderLineItemDao orderLineItemDao, OrderDao orderDao) {
        this.orderLineItemDao = orderLineItemDao;
        this.orderDao = orderDao;
    }

    private Long orderIdA;
    private Long orderIdB;

    @BeforeEach
    void setUp() {
        orderIdA = orderDao.save(new Order(1L, "COOKING", LocalDateTime.now(), null)).getId();
        orderIdB = orderDao.save(new Order(2L, "COOKING", LocalDateTime.now(), null)).getId();
    }

    @Test
    void save() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem(orderIdA, 1L, 2);
        // when
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);
        // then
        assertThat(savedOrderLineItem.getSeq()).isNotNull();
    }

    @Test
    void findById() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem(orderIdA, 1L, 2);
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);

        // when
        Optional<OrderLineItem> foundOrderLineItem = orderLineItemDao.findById(savedOrderLineItem.getSeq());

        // then
        assertThat(foundOrderLineItem).isPresent();
    }

    @Test
    void findAll() {
        // given
        orderLineItemDao.save(new OrderLineItem(orderIdA, 1L, 2));
        orderLineItemDao.save(new OrderLineItem(orderIdB, 2L, 3));

        // when
        List<OrderLineItem> orderLineItems = orderLineItemDao.findAll();

        // then
        assertThat(orderLineItems).hasSize(2);
    }

    @Test
    void findAllByOrderId() {
        // given
        orderLineItemDao.save(new OrderLineItem(orderIdA, 1L, 2));
        orderLineItemDao.save(new OrderLineItem(orderIdA, 2L, 3));

        // when
        List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(orderIdA);

        // then
        assertThat(orderLineItems).hasSize(2);
    }
}
