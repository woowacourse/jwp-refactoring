package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.BeanAssembler;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class OrderLineItemDaoTest {

    @Autowired
    private DataSource dataSource;

    private OrderLineItemDao orderLineItemDao;

    @Autowired
    public OrderLineItemDaoTest(DataSource dataSource) {
        orderLineItemDao = BeanAssembler.createOrderLineItemDao(dataSource);
    }

    private Long orderIdA;
    private Long orderIdB;

    @BeforeEach
    void setUp() {
        OrderDao orderDao = BeanAssembler.createOrderDao(dataSource);
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
