package kitchenpos.dao;

import static kitchenpos.TestObjectFactory.createMenu;
import static kitchenpos.TestObjectFactory.createOrderLineItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest
@Sql(value = "/deleteAll.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class OrderDaoTest {

    @Autowired
    private OrderDao orderDao;

    private Order order;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private MenuDao menuDao;

    @BeforeEach
    void setUp() {
        OrderTable orderTable = OrderTable.builder()
            .empty(false)
            .build();
        Menu menu = createMenu(18_000);
        OrderLineItem orderLineItem = createOrderLineItem(menuDao.save(menu));
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);
        List<OrderLineItem> orderLineItems = Arrays.asList(savedOrderLineItem);

        order = Order.builder()
            .orderTable(orderTableDao.save(orderTable))
            .orderStatus(OrderStatus.COOKING)
            .orderLineItems(orderLineItems)
            .build();
    }

    @DisplayName("OrderStatus 변경 시 버전 증가")
    @Test
    void version_OrderStatus() {
        Order savedOrder = orderDao.save(order);
        savedOrder.changeOrderStatus(OrderStatus.MEAL);
        Order changedOrder = orderDao.saveAndFlush(savedOrder);

        int actual = changedOrder.getVersion() - savedOrder.getVersion();
        assertThat(actual).isEqualTo(1);
    }

    @DisplayName("[예외] 동시에 같은 주문 수정")
    @Test
    void objectOptimisticLockingFailureException() {
        Order savedOrder = orderDao.save(order);
        savedOrder.changeOrderStatus(OrderStatus.MEAL);
        orderDao.saveAndFlush(savedOrder);

        assertThatThrownBy(
            () -> {
                savedOrder.changeOrderStatus(OrderStatus.COMPLETION);
                orderDao.save(savedOrder);
            }
        ).isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }
}