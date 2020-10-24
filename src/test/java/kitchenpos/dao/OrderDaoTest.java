package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.Order;
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

    @BeforeEach
    void setUp() {
        OrderTable orderTable = OrderTable.builder()
            .empty(false)
            .build();

        order = Order.builder()
            .orderTable(orderTableDao.save(orderTable))
            .orderStatus(OrderStatus.COOKING)
            .build();
    }

    @DisplayName("OrderStatus 변경 시 버전 증가")
    @Test
    void version_OrderStatus() {
        Order savedOrder = orderDao.save(order);
        savedOrder.changeOrderStatus(OrderStatus.MEAL);
        Order changedOrder = orderDao.saveAndFlush(savedOrder);

        assertThat(changedOrder.getVersion()).isEqualTo(1);
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