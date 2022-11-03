package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.common.annotation.SpringTestWithData;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("OrderDao 는 ")
@SpringTestWithData
class OrderDaoTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("주문을 저장한다.")
    @Test
    void save() {
        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);
        final Order order = orderDao.save(
                new Order(savedOrderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(),
                        List.of(new OrderLineItem(null, 1L))));

        assertThat(order.getId()).isGreaterThanOrEqualTo(1L);
    }

    @DisplayName("특정 주문을 찾는다.")
    @Test
    void findById() {
        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);
        final Order order = orderDao.save(
                new Order(savedOrderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(),
                        List.of(new OrderLineItem(null, 1L))));

        final Order actual = orderDao.findById(order.getId())
                .get();

        assertThat(actual.getId()).isEqualTo(order.getId());
    }

    @DisplayName("주문 테이블 id로 주문을 찾는다.")
    @Test
    void findByTableId() {
        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);
        final Order savedOrder = orderDao.save(new Order(savedOrderTable.getId(), OrderStatus.MEAL, LocalDateTime.now(),
                List.of(new OrderLineItem(1L, 1L))));

        final Order actual = orderDao.findByTableId(savedOrder.getOrderTableId())
                .get();

        assertThat(actual.getId()).isEqualTo(savedOrder.getId());
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void findAll() {
        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);
        final Order savedOrder = orderDao.save(new Order(savedOrderTable.getId(), OrderStatus.MEAL, LocalDateTime.now(),
                List.of(new OrderLineItem(1L, 1L))));

        final List<OrderTable> orderTables = orderTableDao.findAll();

        assertAll(
                () -> assertThat(orderTables.size()).isEqualTo(1),
                () -> assertThat(orderTables.get(0).getId()).isEqualTo(savedOrder.getId())
        );
    }

    @DisplayName("주문 테이블 id로 주문 항목을 찾는다.")
    @Test
    void findAllByTableIds() {
        final OrderTable orderTable1 = orderTableDao.save(new OrderTable(0, true));
        final OrderTable orderTable2 = orderTableDao.save(new OrderTable(0, true));
        final Order order1 = new Order(orderTable1.getId(), OrderStatus.MEAL, LocalDateTime.now(),
                List.of(new OrderLineItem(1L, 1L)));
        final Order order2 = new Order(orderTable2.getId(), OrderStatus.MEAL, LocalDateTime.now(),
                List.of(new OrderLineItem(1L, 1L)));
        orderDao.save(order1);
        orderDao.save(order2);

        final List<Order> orders = orderDao.findAllByOrderTableId(
                List.of(orderTable1.getId(), orderTable2.getId()));

        assertThat(orders.size()).isEqualTo(2);
    }
}
